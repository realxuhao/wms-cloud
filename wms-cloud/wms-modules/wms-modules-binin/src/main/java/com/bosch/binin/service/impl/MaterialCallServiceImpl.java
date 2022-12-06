package com.bosch.binin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.binin.api.StockLog;
import com.bosch.binin.api.domain.MaterialCall;
import com.bosch.binin.api.domain.MaterialKanban;
import com.bosch.binin.api.domain.Stock;
import com.bosch.binin.api.domain.dto.MaterialCalJobRequestDTO;
import com.bosch.binin.api.domain.dto.MaterialCallDTO;
import com.bosch.binin.api.domain.dto.MaterialCallQueryDTO;
import com.bosch.binin.api.domain.vo.MaterialCallCheckResultVO;
import com.bosch.binin.api.domain.vo.RequirementResultVO;
import com.bosch.binin.api.enumeration.MaterialCallStatusEnum;
import com.bosch.binin.api.enumeration.KanbanActionTypeEnum;
import com.bosch.binin.api.enumeration.MaterialCallSortTypeEnum;
import com.bosch.binin.api.enumeration.KanbanStatusEnum;
import com.bosch.binin.mapper.MaterialCallMapper;
import com.bosch.binin.mapper.MaterialKanbanMapper;
import com.bosch.binin.service.IMaterialCallService;
import com.bosch.binin.service.IMaterialKanbanService;
import com.bosch.binin.service.IStockLogService;
import com.bosch.binin.service.IStockService;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.enums.MoveTypeEnums;
import com.ruoyi.common.core.enums.QualityStatusEnums;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.DoubleMathUtil;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.utils.bean.BeanConverUtil;
import com.ruoyi.common.security.utils.SecurityUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-11-10 14:12
 **/
@Service
@Validated
public class MaterialCallServiceImpl extends ServiceImpl<MaterialCallMapper, MaterialCall> implements IMaterialCallService {
    @Autowired
    private MaterialKanbanMapper materialKanbanMapper;

    @Autowired
    private MaterialCallMapper materialCallMapper;

    @Autowired
    private IStockService stockService;

    @Autowired
    private IStockLogService stockLogService;


    @Autowired
    private IMaterialKanbanService kanbanService;

    @Override
    public List<MaterialCall> getMaterialCallList(MaterialCallQueryDTO queryDTO) {

        LambdaQueryWrapper<MaterialCall> queryWrapper = new LambdaQueryWrapper();
        if (queryDTO != null) {
            queryWrapper.like(StringUtils.isNotEmpty(queryDTO.getMaterialNb()), MaterialCall::getMaterialNb,
                    queryDTO.getMaterialNb())
                    .like(StringUtils.isNotEmpty(queryDTO.getCell()), MaterialCall::getCell, queryDTO.getCell())
                    .like(StringUtils.isNotEmpty(queryDTO.getOrderNb()), MaterialCall::getOrderNb,
                            queryDTO.getOrderNb())
                    .like(StringUtils.isNotEmpty(queryDTO.getCreateBy()), MaterialCall::getCreateBy,
                            queryDTO.getCreateBy())
                    .like(ObjectUtils.isNotEmpty(queryDTO.getStatus()), MaterialCall::getStatus, queryDTO.getStatus())
                    .apply(ObjectUtils.allNotNull(queryDTO.getStartCreateTime()), "date_format (create_time," +
                            "'%Y-%m-%d') >= date_format ({0},'%Y-%m-%d')", queryDTO.getStartCreateTime())
                    .apply(ObjectUtils.allNotNull(queryDTO.getEndCreateTime()), "date_format (create_time,'%Y-%m-%d')" +
                            " <= date_format ({0},'%Y-%m-%d')", queryDTO.getEndCreateTime())
                    .orderByAsc(MaterialCall::getStatus)
                    .orderByDesc(MaterialCall::getCreateTime);
        }
        List<MaterialCall> materialCalls = materialCallMapper.selectList(queryWrapper);

        return materialCalls;
    }


    @Override
    public boolean validList(List<MaterialCallDTO> dtos) {
        List<String> orderNbs = dtos.stream().map(MaterialCallDTO::getOrderNb).collect(Collectors.toList());
        LambdaQueryWrapper<MaterialCall> wrapper = new LambdaQueryWrapper<MaterialCall>();
        wrapper.in(MaterialCall::getOrderNb, orderNbs);
//        dtos.forEach(r->{
//            wrapper.or(wp->wp.eq(MaterialCall::getOrderNb,r.getOrderNb()).eq(MaterialCall::getMaterialNb,r
//            .getMaterialNb()));
//        });

        Integer integer = materialCallMapper.selectCount(wrapper);

        return integer > 0;
    }

    private List<MaterialCallCheckResultVO.NotEnoughStock> dealUnEnoughStock(List<MaterialCall> dos) {

        List<String> materials = dos.stream().map(MaterialCall::getMaterialNb).collect(Collectors.toList());
        LambdaQueryWrapper<Stock> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(Stock::getMaterialNb, materials);
        lambdaQueryWrapper.eq(Stock::getQualityStatus, QualityStatusEnums.USE.getCode());
        lambdaQueryWrapper.eq(Stock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        List<Stock> stockList = stockService.list(lambdaQueryWrapper);
        Map<String, Double> materialAvailableStockMap =
                stockList.stream().collect(Collectors.groupingBy(Stock::getMaterialNb,
                        Collectors.summingDouble(Stock::getAvailableStock)));
        List<MaterialCallCheckResultVO.NotEnoughStock> notEnoughStockList = new ArrayList<>();
        dos.stream().forEach(item -> {
            item.setUnIssuedQuantity(DoubleMathUtil.doubleMathCalculation(item.getQuantity(),
                    item.getIssuedQuantity(), "-"));
            Double requireQuantity = item.getUnIssuedQuantity();
            Double stockQuantity = materialAvailableStockMap.get(item.getMaterialNb());
            if (Objects.isNull(stockQuantity)){
                throw new ServiceException("下发失败，该物料暂时没有库存或库存状态不可用");
            }
            if (requireQuantity > stockQuantity) {
                MaterialCallCheckResultVO.NotEnoughStock notEnoughStock =
                        new MaterialCallCheckResultVO.NotEnoughStock();
                notEnoughStock.setMaterialNb(item.getMaterialNb());
                notEnoughStock.setAvaliableQuantity(stockQuantity);
                notEnoughStockList.add(notEnoughStock);
            }

        });
        return notEnoughStockList;


    }

    @Override
    public RequirementResultVO converToRequirement(List<MaterialCall> dos, boolean continueFlag) {
        if (CollectionUtils.isEmpty(dos)) {
            return null;
        }


        //部分满足的物料号
        List<RequirementResultVO.MaterialOrder> unStatisfiedMaterialNbs = new ArrayList<>();
        //完全满足的物料号
        List<RequirementResultVO.MaterialOrder> fullStatisfiedMaterialNbs = new ArrayList<>();
        //没有库存的物料号
        List<RequirementResultVO.MaterialOrder> noStockMaterialNbs = new ArrayList<>();


        RequirementResultVO requirementResultVO =
                RequirementResultVO.builder().fullStatisfiedMaterialNbs(fullStatisfiedMaterialNbs).noStockMaterialNbs(noStockMaterialNbs).unStatisfiedMaterialNbs(unStatisfiedMaterialNbs).build();

        dos.forEach(item -> {
            item.setUnIssuedQuantity(DoubleMathUtil.doubleMathCalculation(item.getQuantity(),
                    item.getIssuedQuantity(), "-"));

            List<Stock> useMaterialStockList = new ArrayList<>();
            //先查询出来需要用到的结果
            getUseMaterialStock(item, useMaterialStockList, requirementResultVO);
            //处理筛选出来的库存列表
            dealUseMaterialStockList(useMaterialStockList, item);
            item.setUpdateBy(SecurityUtils.getUsername());
            item.setUpdateTime(new Date());

        });

        return RequirementResultVO.builder().fullStatisfiedMaterialNbs(fullStatisfiedMaterialNbs).noStockMaterialNbs(noStockMaterialNbs).unStatisfiedMaterialNbs(unStatisfiedMaterialNbs).build();
    }

    @Override
    public RequirementResultVO systemGenerateJob(MaterialCalJobRequestDTO.SystemGenerateJob systemGenerateJob) {
        if (Objects.isNull(systemGenerateJob) || CollectionUtils.isEmpty(systemGenerateJob.getCallIds())) {
            throw new ServiceException("请选择需求记录后再进行生成");
        }


        LambdaQueryWrapper<MaterialCall> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(MaterialCall::getId, systemGenerateJob.getCallIds());
        queryWrapper.eq(MaterialCall::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        List<MaterialCall> callList = materialCallMapper.selectList(queryWrapper);
        //校验是否含有已经完全下发过的需求
        List<MaterialCall> hasPerformedList =
                callList.stream().filter(item -> item.getStatus() == MaterialCallStatusEnum.FULL_ISSUED.code()).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(hasPerformedList)) {
            throw new ServiceException("包含已经执行过的需求");
        }

        RequirementResultVO requirementResultVO = converToRequirement(callList, systemGenerateJob.isContinueFlag());

        //需要更新需求
        updateBatchById(callList);


        return requirementResultVO;
    }

    @Override
    public int updateCallQuantity(MaterialCallDTO callDTO) {
        if (Objects.isNull(callDTO) || Objects.isNull(callDTO.getId()) || Objects.isNull(callDTO.getQuantity())) {
            throw new ServiceException("id或者quantity不能为空");
        }
        MaterialCall materialCall = materialCallMapper.selectById(callDTO.getId());
        if (materialCall == null || MaterialCallStatusEnum.FULL_ISSUED.code().equals(materialCall.getStatus()) || MaterialCallStatusEnum.PART_ISSUED.code().equals(materialCall.getStatus())) {
            throw new ServiceException("该需求已经下发，不能修改");
        }
        MaterialCall call = BeanConverUtil.conver(callDTO, MaterialCall.class);

        return materialCallMapper.updateById(call);
    }

    @Override
    public MaterialCallCheckResultVO systemGenerateJobCheck(MaterialCalJobRequestDTO.SystemGenerateJob systemGenerateJob) {
        if (Objects.isNull(systemGenerateJob) || CollectionUtils.isEmpty(systemGenerateJob.getCallIds())) {
            throw new ServiceException("请选择需求记录后再进行生成");
        }


        LambdaQueryWrapper<MaterialCall> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(MaterialCall::getId, systemGenerateJob.getCallIds());
        queryWrapper.eq(MaterialCall::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        List<MaterialCall> callList = materialCallMapper.selectList(queryWrapper);
        //校验是否含有已经完全下发过的需求
        List<MaterialCall> hasPerformedList =
                callList.stream().filter(item -> item.getStatus() == MaterialCallStatusEnum.FULL_ISSUED.code()).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(hasPerformedList)) {
            throw new ServiceException("包含已经执行过的需求");
        }
        List<MaterialCallCheckResultVO.NotEnoughStock> notEnoughStockList = dealUnEnoughStock(callList);
        MaterialCallCheckResultVO callCheckResultVO = new MaterialCallCheckResultVO();
        if (CollectionUtils.isEmpty(notEnoughStockList)) {
            callCheckResultVO.setCheckFlag(true);
            return callCheckResultVO;
        }
        callCheckResultVO.setNotEnoughStockList(notEnoughStockList);
        callCheckResultVO.setCheckFlag(false);
        return callCheckResultVO;
    }

    @Override
    public int updateCallStatus(MaterialCall materialCallNew) {
        //获取叫料表中的数据
        LambdaQueryWrapper<MaterialCall> qw = new LambdaQueryWrapper<>();
        qw.eq(MaterialCall::getOrderNb, materialCallNew.getOrderNb());
        qw.eq(MaterialCall::getMaterialNb, materialCallNew.getMaterialNb());
        qw.eq(MaterialCall::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        qw.last("for update");
        MaterialCall materialCallDB = materialCallMapper.selectOne(qw);

        if (materialCallDB == null) {
            throw new ServiceException("未查询到相同订单号、物料号的叫料需求");
        }
        double newQuantity = DoubleMathUtil.doubleMathCalculation(materialCallNew.getIssuedQuantity(),
                materialCallDB.getIssuedQuantity(), "+");

        //更新叫料表
        if (materialCallDB.getQuantity() <= newQuantity) {
            materialCallDB.setStatus(MaterialCallStatusEnum.FULL_ISSUED.code());
        } else {
            materialCallDB.setStatus(MaterialCallStatusEnum.PART_ISSUED.code());
        }
        materialCallDB.setIssuedQuantity(newQuantity);
        return materialCallMapper.updateById(materialCallDB);
    }

    @Override
    public int updateCallStatus(MaterialCall materialCallNew, MaterialKanban dto) {
        //获取叫料表中的数据
        LambdaQueryWrapper<MaterialCall> qw = new LambdaQueryWrapper<>();
        qw.eq(MaterialCall::getOrderNb, materialCallNew.getOrderNb());
        qw.eq(MaterialCall::getMaterialNb, materialCallNew.getMaterialNb());
        qw.eq(MaterialCall::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        qw.last("for update");
        MaterialCall materialCallDB = materialCallMapper.selectOne(qw);
        if (materialCallDB == null) {
            throw new ServiceException("未查询到相关订单号:"+materialCallNew.getOrderNb()+".物料号:"+materialCallNew.getMaterialNb()+"的叫料需求");
        }
        //获取看kanban数据
        LambdaQueryWrapper<MaterialKanban> kanbanqw = new LambdaQueryWrapper<>();
        kanbanqw.eq(MaterialKanban::getSsccNumber, dto.getSsccNumber());
        kanbanqw.ne(MaterialKanban::getStatus, KanbanStatusEnum.CANCEL);
        kanbanqw.eq(MaterialKanban::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        kanbanqw.last("for update");
        MaterialKanban materialKanban = materialKanbanMapper.selectOne(kanbanqw);
        if (materialKanban == null) {
            throw new ServiceException("未查询到相关sscc码"+dto.getSsccNumber()+"的kanban任务");
        }
        //下发量=需求原下发量-kanban原数量+kanban修改后数量
        double newQuantity = DoubleMathUtil.doubleMathCalculation(materialCallDB.getIssuedQuantity(),materialKanban.getQuantity()
                , "-");
        newQuantity =DoubleMathUtil.doubleMathCalculation(newQuantity,materialCallNew.getIssuedQuantity(),"+");
        //更新叫料表
        if (materialCallDB.getQuantity() <= newQuantity) {
            materialCallDB.setStatus(MaterialCallStatusEnum.FULL_ISSUED.code());
        } else {
            materialCallDB.setStatus(MaterialCallStatusEnum.PART_ISSUED.code());
        }
        materialCallDB.setIssuedQuantity(newQuantity);
        return materialCallMapper.updateById(materialCallDB);
    }

    @Override
    public int updateCallQuantity(MaterialKanban kanban) {

        //查询call数据
        LambdaQueryWrapper<MaterialCall> qw = new LambdaQueryWrapper<MaterialCall>();
        qw.eq(MaterialCall::getOrderNb, kanban.getOrderNumber());
        qw.eq(MaterialCall::getMaterialNb, kanban.getMaterialCode());
        qw.eq(MaterialCall::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        qw.last("for update");
        MaterialCall materialCall = materialCallMapper.selectOne(qw);
        if (materialCall == null) {
            throw new ServiceException("未查询到kanban数据");
        }
        //修改下发量 状态
        double newQuantity = DoubleMathUtil.doubleMathCalculation(materialCall.getIssuedQuantity(),
                kanban.getQuantity(), "-");
        //赋值
        if (newQuantity <= 0) {
            //下发量小于取消量
            materialCall.setStatus(MaterialCallStatusEnum.WAITING_ISSUE.code());
        } else {
            //下发量大于取消量
            materialCall.setStatus(MaterialCallStatusEnum.PART_ISSUED.code());
        }
        materialCall.setIssuedQuantity(newQuantity);
        //更新叫料表
        return materialCallMapper.updateById(materialCall);
    }

    @Override
    public void deleteRequirement(List<Long> ids) {
        LambdaQueryWrapper<MaterialCall> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(MaterialCall::getId, ids);

        List<MaterialCall> materialCalls = materialCallMapper.selectList(queryWrapper);


        List<MaterialCall> list =
                materialCalls.stream().filter(item -> item.getStatus().equals(MaterialCallStatusEnum.WAITING_ISSUE.code()) && item.getDeleteFlag().equals(DeleteFlagStatus.FALSE.getCode())).collect(Collectors.toList());


        if (CollectionUtils.isEmpty(list) || CollectionUtils.isEmpty(list) || list.size() != materialCalls.size()) {
            throw new ServiceException("只允许修改未下发状态的需求！");
        }

        materialCalls.stream().forEach(item -> item.setDeleteFlag(DeleteFlagStatus.TRUE.getCode()));

        updateBatchById(materialCalls);

    }


    private void dealUseMaterialStockList(List<Stock> useMaterialStockList, MaterialCall call) {
        if (CollectionUtils.isEmpty(useMaterialStockList)) {
            return;
        }
        //计算最后一托是整托还是拆托
        Double stockSum = useMaterialStockList.stream().collect(Collectors.summingDouble(Stock::getAvailableStock));
        Double deviation = null;
        boolean splitFlag = false;
        if (stockSum > call.getUnIssuedQuantity()) {
            splitFlag = true;
            deviation = DoubleMathUtil.doubleMathCalculation(stockSum, call.getUnIssuedQuantity(), "-");
        }

        List<MaterialKanban> kanbanList = new ArrayList<>();
        for (int i = 0; i < useMaterialStockList.size(); i++) {
            Stock stock = useMaterialStockList.get(i);
            MaterialKanban kanban = new MaterialKanban();
            kanban.setOrderNumber(call.getOrderNb());
            kanban.setFactoryCode(stock.getPlantNb());
            kanban.setWareCode(stock.getWareCode());
            kanban.setAreaCode(stock.getAreaCode());
            kanban.setBinCode(stock.getBinCode());
            kanban.setMaterialCode(stock.getMaterialNb());
            kanban.setSsccNumber(stock.getSsccNumber());
            kanban.setCell(call.getCell());
            kanban.setType(KanbanActionTypeEnum.FULL_BIN_DOWN.value());
            kanban.setQuantity(stock.getAvailableStock());
            kanban.setStatus(KanbanStatusEnum.WAITING_ISSUE.value());
            kanban.setCreateBy(SecurityUtils.getUsername());
            kanban.setCreateTime(new Date());
            kanban.setMoveType(MoveTypeEnums.CALL.getCode());


            if (i == useMaterialStockList.size() - 1 && splitFlag) {
                kanban.setQuantity(DoubleMathUtil.doubleMathCalculation(stock.getAvailableStock(), deviation, "-"));
                kanban.setType(splitFlag ? KanbanActionTypeEnum.PART_BIN_DOWN.value() :
                        KanbanActionTypeEnum.FULL_BIN_DOWN.value());
                stock.setFreezeStock(stock.getFreezeStock() + kanban.getQuantity());
                stock.setAvailableStock(stock.getAvailableStock() - kanban.getQuantity());
            } else {
                stock.setFreezeStock(stock.getFreezeStock() + stock.getAvailableStock());
                stock.setAvailableStock(Double.valueOf(0));

            }
            kanbanList.add(kanban);
        }
        //批量添加kanban
        kanbanService.saveBatch(kanbanList);
        //更新库存
        stockService.updateBatchById(useMaterialStockList);
        //批量添加库存日志表
        List<StockLog> stockLogList = BeanConverUtil.converList(useMaterialStockList, StockLog.class);
        stockLogList.forEach(item -> {
            item.setMoveType(MoveTypeEnums.CALL.getCode());
        });
        stockLogService.saveBatch(stockLogList);

    }


    private List<Stock> getUseMaterialByNb(String materialNb) {
        LambdaQueryWrapper<Stock> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Stock::getMaterialNb, materialNb);
        lambdaQueryWrapper.eq(Stock::getQualityStatus, QualityStatusEnums.USE.getCode());
        lambdaQueryWrapper.eq(Stock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        lambdaQueryWrapper.eq(Stock::getFreezeStock,0);
        List<Stock> stockList = stockService.list(lambdaQueryWrapper);
        if (CollectionUtils.isEmpty(stockList)) {
            stockList = new ArrayList<>();
        }
        return stockList;
    }

    private void getUseMaterialStock(MaterialCall call, List<Stock> useMaterialStockList,
                                     RequirementResultVO requirementResultVO) {
        //根据库存列表查询出来
        List<Stock> stockList = getUseMaterialByNb(call.getMaterialNb());
        List<Stock> sortedStockList = new ArrayList<>();
        if (MaterialCallSortTypeEnum.BBD_FIRST.value().equals(call.getSortType())) {
            sortedStockList =
                    stockList.stream().filter(item -> item.getAvailableStock() != 0).
                            sorted(Comparator.comparing(Stock::getExpireDate).
                                    thenComparing(Stock::getPlantNb)).collect(Collectors.toList());
        } else if (MaterialCallSortTypeEnum.MAIN_WARE_FIRST.value().equals(call.getSortType())) {
            sortedStockList =
                    stockList.stream().filter(item -> item.getAvailableStock() != 0).sorted(Comparator.comparing(Stock::getPlantNb).thenComparing(Stock::getExpireDate)).collect(Collectors.toList());
        }
        if (CollectionUtils.isEmpty(sortedStockList)) {
            requirementResultVO.getNoStockMaterialNbs().add(RequirementResultVO.MaterialOrder.builder().materialNb(call.getMaterialNb()).orderNb(call.getOrderNb()).build());
            return;
        }
        double count = 0;
        for (Stock stock : sortedStockList) {
            count = DoubleMathUtil.doubleMathCalculation(count, stock.getAvailableStock(), "+");
            useMaterialStockList.add(stock);
            if (count >= call.getUnIssuedQuantity()) {
                break;
            }
        }
        if (count == 0) {
            call.setIssuedQuantity(call.getIssuedQuantity());
            requirementResultVO.getNoStockMaterialNbs().add(RequirementResultVO.MaterialOrder.builder().materialNb(call.getMaterialNb()).orderNb(call.getOrderNb()).build());

        }
        //计算部分满足还是全部满足
        //完全满足
        if (count >= call.getUnIssuedQuantity()) {
            call.setIssuedQuantity(DoubleMathUtil.doubleMathCalculation(call.getUnIssuedQuantity(),
                    call.getIssuedQuantity(), "+"));
            requirementResultVO.getFullStatisfiedMaterialNbs().add(RequirementResultVO.MaterialOrder.builder().materialNb(call.getMaterialNb()).orderNb(call.getOrderNb()).build());
            call.setStatus(MaterialCallStatusEnum.FULL_ISSUED.code());
        }
        //部分满足
        if (count != 0 && count < call.getUnIssuedQuantity()) {
            call.setIssuedQuantity(DoubleMathUtil.doubleMathCalculation(call.getIssuedQuantity(), count, "+"));
            requirementResultVO.getUnStatisfiedMaterialNbs().add(RequirementResultVO.MaterialOrder.builder().materialNb(call.getMaterialNb()).orderNb(call.getOrderNb()).build());
            call.setStatus(MaterialCallStatusEnum.PART_ISSUED.code());

        }
    }


}
