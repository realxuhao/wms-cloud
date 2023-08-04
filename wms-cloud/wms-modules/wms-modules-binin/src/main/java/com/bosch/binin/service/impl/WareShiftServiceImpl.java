package com.bosch.binin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.binin.api.domain.*;
import com.bosch.binin.api.domain.dto.*;
import com.bosch.binin.api.domain.vo.BinInVO;
import com.bosch.binin.api.domain.vo.RequirementResultVO;
import com.bosch.binin.api.domain.vo.StockVO;
import com.bosch.binin.api.domain.vo.WareShiftVO;
import com.bosch.binin.api.enumeration.*;
import com.bosch.binin.mapper.IQCSamplePlanMapper;
import com.bosch.binin.mapper.MaterialKanbanMapper;
import com.bosch.binin.mapper.WareShiftMapper;
import com.bosch.binin.service.*;
import com.bosch.binin.utils.BeanConverUtil;
import com.bosch.masterdata.api.RemoteMaterialService;
import com.bosch.masterdata.api.domain.Ware;
import com.bosch.masterdata.api.domain.vo.BinVO;
import com.bosch.masterdata.api.domain.vo.MaterialVO;
import com.bosch.masterdata.api.enumeration.AreaTypeEnum;
import com.bosch.system.api.domain.UserOperationLog;
import com.ruoyi.common.core.constant.AreaListConstants;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.enums.MoveTypeEnums;
import com.ruoyi.common.core.enums.QualityStatusEnums;
import com.ruoyi.common.core.enums.StatusEnums;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.DoubleMathUtil;
import com.ruoyi.common.core.utils.MesBarCodeUtil;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.log.enums.MaterialType;
import com.ruoyi.common.log.enums.UserOperationType;
import com.ruoyi.common.log.service.IUserOperationLogService;
import com.ruoyi.common.security.utils.SecurityUtils;
import org.apache.catalina.User;
import org.apache.poi.ss.formula.functions.IDStarAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;


import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-11-16 20:48
 **/
@Service
public class WareShiftServiceImpl extends ServiceImpl<WareShiftMapper, WareShift> implements IWareShiftService {


    @Autowired
    private IStockService stockService;

    @Autowired
    private WareShiftMapper wareShiftMapper;

    @Autowired
    private IBinInService binInService;

    @Autowired
    private MaterialKanbanMapper kanbanMapper;

    @Autowired
    @Lazy //懒加载
    private IMaterialKanbanService kanbanService;

    @Autowired
    @Lazy
    private IIQCSamplePlanService samplePlanService;

    @Autowired
    private IMaterialCallService callService;

    @Autowired
    private RemoteMaterialService remoteMaterialService;

    @Autowired
    private ITranshipmentOrderService transhipmentOrderService;


    @Autowired
    private IUserOperationLogService operationLogService;

    @Override
    public Boolean addShiftRequirement(AddShiftTaskDTO dto) {
        List<String> ssccNbList = dto.getSsccNbList();
        if (CollectionUtils.isEmpty(ssccNbList)) {
            throw new ServiceException("请选择库存");
        }

        //先去再次校验一下库存
        LambdaQueryWrapper<Stock> stockQueryWrapper = new LambdaQueryWrapper<>();
        stockQueryWrapper.in(Stock::getSsccNumber, ssccNbList);
        stockQueryWrapper.eq(Stock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        stockQueryWrapper.eq(Stock::getQualityStatus, QualityStatusEnums.USE.getCode());
        List<Stock> stockList = stockService.list(stockQueryWrapper);
        if (CollectionUtils.isEmpty(stockList) || stockList.size() != ssccNbList.size()) {
            throw new ServiceException("库存状态发生变更，请刷新页面");
        }

        List<WareShift> wareShiftList = new ArrayList<>();

        stockList.stream().forEach(item -> {
            WareShift wareShift = WareShift.builder().sourcePlantNb(item.getPlantNb()).sourceWareCode(item.getWareCode()).sourceAreaCode(item.getAreaCode())
                    .sourceBinCode(item.getBinCode()).materialNb(item.getMaterialNb()).batchNb(item.getBatchNb()).expireDate(item.getExpireDate())
                    .ssccNb(item.getSsccNumber()).deleteFlag(DeleteFlagStatus.FALSE.getCode()).moveType(MoveTypeEnums.WARE_SHIFT.getCode())
                    .status(KanbanStatusEnum.WAITING_BIN_DOWN.value())
                    .quantity(item.getTotalStock())
                    .build();
            wareShiftList.add(wareShift);
            //更新冻结库存
            item.setFreezeStock(item.getFreezeStock() + item.getAvailableStock());
            item.setAvailableStock(item.getTotalStock() - item.getFreezeStock());

        });
        //冻结库存，更新状态
        stockService.updateBatchById(stockList);
        return saveBatch(wareShiftList);


    }

    @Override
    public void binDown(String mesBarCode) {
        String ssccNb = MesBarCodeUtil.getSSCC(mesBarCode);
        LambdaQueryWrapper<WareShift> wareShiftLambdaQueryWrapper = new LambdaQueryWrapper<>();
        wareShiftLambdaQueryWrapper.eq(WareShift::getSsccNb, ssccNb);
        wareShiftLambdaQueryWrapper.eq(WareShift::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        wareShiftLambdaQueryWrapper.ne(WareShift::getStatus, KanbanStatusEnum.CANCEL.value());
        wareShiftLambdaQueryWrapper.ne(WareShift::getStatus, KanbanStatusEnum.FINISH.value());

        wareShiftLambdaQueryWrapper.last("for update");
        WareShift wareShift = wareShiftMapper.selectOne(wareShiftLambdaQueryWrapper);
        if (Objects.isNull(wareShift)) {
            throw new ServiceException("该SSCC码 " + ssccNb + " 不存在移库任务");
        }
        if (!KanbanStatusEnum.WAITING_BIN_DOWN.value().equals(wareShift.getStatus())) {
            throw new ServiceException("该SSCC码 " + ssccNb + "对应任务状态为: " + KanbanStatusEnum.getDesc(String.valueOf(wareShift.getStatus())) + " 不可下架 ");

        }

//        //在kanban任务中查询
//        LambdaQueryWrapper<MaterialKanban> kanbanQueryWrapper = new LambdaQueryWrapper<>();
//        kanbanQueryWrapper.eq(MaterialKanban::getSsccNumber, ssccNb);
//        kanbanQueryWrapper.eq(MaterialKanban::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
//        kanbanQueryWrapper.eq(MaterialKanban::getStatus, KanbanStatusEnum.WAITING_BIN_DOWN.value());
//        MaterialKanban materialKanban = kanbanMapper.selectOne(kanbanQueryWrapper);
//        //kanban任务修改状态
//        if (materialKanban != null) {
//            materialKanban.setStatus(KanbanStatusEnum.OUT_DOWN.value());
//            kanbanMapper.updateById(materialKanban);
//        }

        //状态修改为外库已下架
        wareShift.setStatus(KanbanStatusEnum.OUT_DOWN.value());
        wareShiftMapper.updateById(wareShift);

        //执行下架
        binInService.binDown(ssccNb);
    }

    @Override
    public BinInVO allocateBin(String mesBarCode, String wareCode) {
        String sscc = MesBarCodeUtil.getSSCC(mesBarCode);
        LambdaQueryWrapper<WareShift> qw = new LambdaQueryWrapper<>();
        qw.eq(WareShift::getSsccNb, sscc);
        qw.eq(WareShift::getStatus, KanbanStatusEnum.INNER_BIN_IN.value());
        qw.eq(WareShift::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        qw.ne(WareShift::getStatus, KanbanStatusEnum.CANCEL.value());
        qw.ne(WareShift::getStatus, KanbanStatusEnum.FINISH.value());

        qw.last("limit 1");
        qw.last("for update");
        WareShift wareShift = wareShiftMapper.selectOne(qw);

        if (Objects.isNull(wareShift)) {
            throw new ServiceException("该SSCC码 " + sscc + " 不存在移库任务");
        }
        if (!KanbanStatusEnum.INNER_BIN_IN.value().equals(wareShift.getStatus())) {
            throw new ServiceException("该SSCC码 " + sscc + "对应任务状态为: " + KanbanStatusEnum.getDesc(String.valueOf(wareShift.getStatus())) + " 不可分配库位 ");

        }
        //分配库位信息
        BinInVO binInVO = binInService.generateInTaskByOldStock(MesBarCodeUtil.getSSCC(mesBarCode), Double.valueOf(0), wareCode);

        return binInVO;
    }

    @Override
    public List<WareShiftVO> getWareShiftList(WareShiftQueryDTO queryDTO) {

        List<WareShiftVO> list = wareShiftMapper.list(queryDTO);
        return list;
    }

    @Override
    public List<WareShiftVO> getWaitingBinIn() {
        List<WareShiftVO> wareShiftVOS = wareShiftMapper.getWaitingBinIn(SecurityUtils.getWareCode());
        return wareShiftVOS;
    }

    @Override
    public void cancelWareShift(Long id) {
        LambdaQueryWrapper<WareShift> wareShiftQueryWrapper = new LambdaQueryWrapper<>();
        WareShift wareShift = wareShiftMapper.selectById(id);

        if (wareShift == null || wareShift.getDeleteFlag().equals(DeleteFlagStatus.TRUE.getCode())) {
            throw new ServiceException("移库任务不存在或者已删除");
        }
        if (wareShift.getStatus().equals(KanbanStatusEnum.CANCEL.value()) ||
                wareShift.getStatus().equals(KanbanStatusEnum.FINISH.value()) ||
                wareShift.getStatus().equals(KanbanStatusEnum.INNER_RECEIVING.value()) ||
                wareShift.getStatus().equals(KanbanStatusEnum.INNER_BIN_IN.value())) {
            throw new ServiceException("当前状态为： " + KanbanStatusEnum.getDesc(wareShift.getStatus().toString()) + " 不可以取消");
        }
        //待下架状态，需要判断是主库待下架还是外库待下架
//        MaterialKanban kanban = kanbanService.getOneBySCAndStatus(wareShift.getSsccNb(), KanbanStatusEnum.WAITING_ISSUE.value());

        if (KanbanStatusEnum.WAITING_BIN_DOWN.value().equals(wareShift.getStatus())) {

            Stock stock = stockService.getAvailablesStockBySscc(wareShift.getSsccNb());
            if (stock != null) {
                stock.setAvailableStock(stock.getTotalStock());
                stock.setFreezeStock(Double.valueOf(0));
                stockService.updateById(stock);

                //看IQC里面有没有。
                LambdaQueryWrapper<IQCSamplePlan> iqcQueryWrapper = new LambdaQueryWrapper<>();
                iqcQueryWrapper.eq(IQCSamplePlan::getSsccNb, stock.getSsccNumber());
                iqcQueryWrapper.eq(IQCSamplePlan::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
                iqcQueryWrapper.eq(IQCSamplePlan::getStatus, IQCStatusEnum.WARE_SHIFTING.code());
                iqcQueryWrapper.last("limit 1");
                IQCSamplePlan samplePlan = samplePlanService.getOne(iqcQueryWrapper);
                if (Objects.nonNull(samplePlan)) {
                    samplePlan.setStatus(IQCStatusEnum.CANCEL.code());
                    samplePlanService.updateById(samplePlan);
                }


            }
        }
        //外库待发运，取消后直接在外库上架
        if (KanbanStatusEnum.OUT_DOWN.value().equals(wareShift.getStatus())) {
            binInService.generateInTaskByOldStock(wareShift.getSsccNb(), Double.valueOf(0), wareShift.getSourceWareCode());

        }

        wareShift.setStatus(KanbanStatusEnum.CANCEL.value());
        wareShiftMapper.updateById(wareShift);


    }

    @Override
    public WareShift getWareShiftBySsccAndStatus(String sscc) {
        LambdaQueryWrapper<WareShift> uw = new LambdaQueryWrapper<>();
        uw.in(WareShift::getSsccNb, sscc);
        uw.ne(WareShift::getStatus, KanbanStatusEnum.CANCEL.value());
        uw.ne(WareShift::getStatus, KanbanStatusEnum.FINISH.value());

        uw.eq(WareShift::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        uw.last("limit 1");
        return wareShiftMapper.selectOne(uw);
    }

    @Override
    public List<WareShift> getListBySSCC(List<String> ssccs) {
        LambdaQueryWrapper<WareShift> qw = new LambdaQueryWrapper<>();
        qw.in(WareShift::getSsccNb, ssccs);
        qw.eq(WareShift::getStatus, KanbanStatusEnum.OUT_DOWN.value());
        qw.eq(WareShift::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        List<WareShift> wareShifts = wareShiftMapper.selectList(qw);
        return wareShifts;
    }

    @Override
    public Boolean add(AddShiftTaskDTO dto) {
        List<String> ssccNbList = dto.getSsccNbList();
        String targetPlantNb = dto.getTargetPlantNb();
        String targetWareCode = dto.getTargetWareCode();
        List<WareShift> wareShiftList = new ArrayList<>();
        LambdaQueryWrapper<Stock> stockQw = new LambdaQueryWrapper<>();
        stockQw.in(Stock::getSsccNumber, ssccNbList);
        stockQw.eq(Stock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        stockQw.eq(Stock::getFreezeStock, Double.valueOf(0));
        List<Stock> stockList = stockService.list(stockQw);
        if (CollectionUtils.isEmpty(stockList) || stockList.size() != ssccNbList.size()) {
            throw new ServiceException("库存数据过期，请刷新后重试");
        }
        stockList.stream().forEach(item -> {
            if (targetWareCode.equals(item.getWareCode())) {
                throw new ServiceException("目的仓库不能属于源仓库");
            }
            WareShift wareShift = new WareShift();
            wareShift.setSourcePlantNb(item.getPlantNb());
            wareShift.setSourceWareCode(item.getWareCode());
            wareShift.setSourceAreaCode(item.getAreaCode());
            wareShift.setSourceBinCode(item.getBinCode());
            wareShift.setMoveType(MoveTypeEnums.WARE_SHIFT.getCode());
            wareShift.setSsccNb(item.getSsccNumber());
            wareShift.setMaterialNb(item.getMaterialNb());
            wareShift.setBatchNb(item.getBatchNb());
            wareShift.setExpireDate(item.getExpireDate());
            wareShift.setTargetPlant(targetPlantNb);
            wareShift.setTargetWareCode(targetWareCode);
            wareShift.setQuantity(item.getTotalStock());
            wareShift.setSplitType(0);
            wareShift.setStatus(KanbanStatusEnum.WAITING_BIN_DOWN.value());
            wareShift.setType(WareShiftTypeEnum.NORMAL.code());

            wareShiftList.add(wareShift);

            item.setFreezeStock(item.getTotalStock());
            item.setAvailableStock(DoubleMathUtil.doubleMathCalculation(item.getTotalStock(), item.getAvailableStock(), "-"));
        });
        stockService.updateBatchById(stockList);
        return saveBatch(wareShiftList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void performBinIn(BinInDTO binInDTO) {

        String mesBarCode = binInDTO.getMesBarCode();
        String sscc = MesBarCodeUtil.getSSCC(mesBarCode);
        LambdaQueryWrapper<WareShift> wareShiftQueryWrapper = new LambdaQueryWrapper<>();
        wareShiftQueryWrapper.eq(WareShift::getSsccNb, sscc);
        wareShiftQueryWrapper.eq(WareShift::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        wareShiftQueryWrapper.ne(WareShift::getStatus, KanbanStatusEnum.CANCEL.value());
        wareShiftQueryWrapper.ne(WareShift::getStatus, KanbanStatusEnum.FINISH.value());

        WareShift wareShift = wareShiftMapper.selectOne(wareShiftQueryWrapper);
        if (wareShift == null) {
            throw new ServiceException("没有该sscc:" + sscc + "对应的移库待上架任务");
        }
        if (wareShift.getStatus() != KanbanStatusEnum.INNER_BIN_IN.value()) {
            throw new ServiceException("sscc:" + sscc + "对应任务状态为:" + KanbanStatusEnum.getDesc(String.valueOf(wareShift.getStatus())) + ",不可上架");
        }

        Stock lastOneBySSCC = new Stock();
        if (wareShift.getSplitType() == 1) {
            lastOneBySSCC = stockService.getOneStock(wareShift.getSourceSscc());
        } else {
            StockVO oneBySSCC = stockService.getLastOneBySSCC(sscc);
            lastOneBySSCC = BeanConverUtil.conver(oneBySSCC, Stock.class);
        }


        BinInVO binInVO = binInService.performBinIn(binInDTO, lastOneBySSCC.getQualityStatus());
        //更新移库任务
        wareShift.setStatus(KanbanStatusEnum.FINISH.value());
        wareShift.setTargetPlant(binInVO.getPlantNb());
        wareShift.setTargetWareCode(binInVO.getWareCode());
        wareShift.setTargetAreaCode(binInVO.getAreaCode());
        wareShift.setTargetBinCode(binInVO.getActualBinCode());
        wareShiftMapper.updateById(wareShift);

//        //如果是看板所产生的移库任务，看板任务也要随之更新
//        //如果是kanban任务
//        LambdaQueryWrapper<MaterialKanban> kanbanQueryWrapper = new LambdaQueryWrapper<>();
//        //待下架任务,该kanban状态，待下架
//        kanbanQueryWrapper.eq(MaterialKanban::getSsccNumber, sscc);
//        kanbanQueryWrapper.eq(MaterialKanban::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
//        kanbanQueryWrapper.ne(MaterialKanban::getStatus, KanbanStatusEnum.FINISH.value());
//        kanbanQueryWrapper.ne(MaterialKanban::getStatus, KanbanStatusEnum.CANCEL.value());
//
//        kanbanQueryWrapper.last("limit 1");
//        kanbanQueryWrapper.last("for update");
//        MaterialKanban materialKanban = kanbanMapper.selectOne(kanbanQueryWrapper);
//        if (!Objects.isNull(materialKanban)) {
//            materialKanban.setStatus(KanbanStatusEnum.WAITING_BIN_DOWN.value());
//            materialKanban.setFactoryCode(binInVO.getPlantNb());
//            materialKanban.setWareCode(binInVO.getWareCode());
//            materialKanban.setAreaCode(binInVO.getAreaCode());
//            materialKanban.setBinCode(binInVO.getActualBinCode());
//            kanbanMapper.updateById(materialKanban);
//            //需要冻结库存
//            LambdaQueryWrapper<Stock> stockQw = new LambdaQueryWrapper<>();
//            stockQw.eq(Stock::getSsccNumber, sscc);
//            stockQw.eq(Stock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
//            Stock stock = stockService.getOne(stockQw);
//
//            stock.setFreezeStock(materialKanban.getQuantity());
//            stock.setAvailableStock(DoubleMathUtil.doubleMathCalculation(stock.getTotalStock(), stock.getFreezeStock(), "-"));
//            stockService.updateById(stock);
//
//        }

//        //移库上架完后，看是不是IQC任务，如果是IQC任务，把IQC任务状态修改为待抽样
//        LambdaQueryWrapper<IQCSamplePlan> iqcQueryWrapper = new LambdaQueryWrapper<>();
//        //待下架任务,该kanban状态，待下架
//        iqcQueryWrapper.eq(IQCSamplePlan::getSsccNb, sscc);
//        iqcQueryWrapper.eq(IQCSamplePlan::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
//        IQCSamplePlan samplePlan = samplePlanMapper.selectOne(iqcQueryWrapper);
//        if (samplePlan != null) {
//            samplePlan.setStatus(IQCStatusEnum.WAITING_SAMPLE.code());
//            samplePlan.setBinDownCode(binInVO.getActualBinCode());
//            samplePlan.setWareCode(binInVO.getWareCode());
//            samplePlanMapper.updateById(samplePlan);
//        }


    }

    @Override
    public Double getInTransitCount(String materialNb) {
        LambdaQueryWrapper<WareShift> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WareShift::getMaterialNb, materialNb);
        queryWrapper.ne(WareShift::getStatus, KanbanStatusEnum.CANCEL.value());
        queryWrapper.ne(WareShift::getStatus, KanbanStatusEnum.FINISH.value());
        List<WareShift> list = this.list(queryWrapper);
        list.stream().forEach(item -> {
            if (item.getSplitType() == 1) {
                item.setQuantity(item.getSplitQuality());
            }
        });
        if (CollectionUtils.isEmpty(list)) {
            return Double.valueOf(0);
        }
        double sum = list.stream().mapToDouble(WareShift::getQuantity).sum();

        return sum;
    }

    @Override
    public void generateWareShiftByCall(List<CallWareShiftDTO> dtos) {
        if (CollectionUtils.isEmpty(dtos)) {
            return;
        }
        dtos.forEach(item -> {
            if (item.getShiftQuality() == null || item.getShiftQuality() <= 0) {
                throw new ServiceException("移库数量必须大于0");
            }
        });
        List<Long> callIds = dtos.stream().map(CallWareShiftDTO::getCallId).collect(Collectors.toList());
        LambdaQueryWrapper<MaterialCall> callQueryWrapper = new LambdaQueryWrapper<>();
        callQueryWrapper.in(MaterialCall::getId, callIds);
        callQueryWrapper.eq(MaterialCall::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        List<MaterialCall> callList = callService.list(callQueryWrapper);
        Map<Long, Double> callQuantityMap =
                dtos.stream().collect(Collectors.groupingBy(CallWareShiftDTO::getCallId,
                        Collectors.summingDouble(CallWareShiftDTO::getShiftQuality)));
        List<WareShift> wareShiftList = new ArrayList<>();
        callList = callList.stream().filter(item -> item.getShiftFlag() == 0).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(callList)) {
            throw new ServiceException("当前选择的数据都创建过移库！请重新选择");
        }
        Map<String, List<MaterialCall>> matrialCallMap = callList.stream().collect(Collectors.groupingBy(MaterialCall::getMaterialNb));

        Map<String, Double> materialCountMap = new HashMap<>();
        Map<String, String> materialCallIds = new HashMap<>();
        Map<String, String> materialCallOrders = new HashMap<>();

        matrialCallMap.forEach((materialNb, list) -> {
            double sum = list.stream().mapToDouble(item -> callQuantityMap.get(item.getId())).sum();
            materialCountMap.put(materialNb, sum);
            List<String> ids = new ArrayList<>();
            list.stream().forEach(item -> ids.add(item.getId().toString()));
            String join = String.join(",", ids);
            materialCallIds.put(materialNb, join);


            List<String> orderList = new ArrayList<>();
            list.stream().forEach(item -> orderList.add(item.getOrderNb().toString()));
            materialCallOrders.put(materialNb, String.join(",", orderList));


            List<WareShift> wareShifts = gennerateWareShift(materialNb, materialCountMap.get(materialNb), materialCallIds.get(materialNb), materialCallOrders.get(materialNb));
            wareShiftList.addAll(wareShifts);
        });


        callList.stream().forEach(call -> {
            call.setShiftFlag(1);
        });
        this.saveBatch(wareShiftList);
        callService.updateBatchById(callList);

    }

    @Override
    public void splitPallet(SplitPalletDTO splitPallet) {
        String sourceSsccNb = splitPallet.getSourceSsccNb();
        LambdaQueryWrapper<WareShift> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WareShift::getSsccNb, sourceSsccNb);
        queryWrapper.ne(WareShift::getStatus, KanbanStatusEnum.FINISH.value());
        queryWrapper.ne(WareShift::getStatus, KanbanStatusEnum.CANCEL.value());
        queryWrapper.last("limit 1");
        WareShift wareShift = this.getOne(queryWrapper);
        if (wareShift.getQuantity() < splitPallet.getSplitQuantity()) {
            throw new ServiceException("拆托数量不能大于总数量");
        }
        //老的拆托任务结束。解冻库存
        wareShift.setStatus(KanbanStatusEnum.FINISH.value());
        this.updateById(wareShift);
        LambdaQueryWrapper<Stock> stockWrapper = new LambdaQueryWrapper<>();
        stockWrapper.eq(Stock::getSsccNumber, sourceSsccNb);
        stockWrapper.eq(Stock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        Stock stock = stockService.getOne(stockWrapper);
        stock.setTotalStock(stock.getTotalStock() - splitPallet.getSplitQuantity());
        stock.setFreezeStock(Double.valueOf(0));
        stock.setAvailableStock(stock.getTotalStock());
        stock.setWholeFlag(StockWholeFlagEnum.NOT_WHOLE.code());

        if (stock.getTotalStock().equals(Double.valueOf(0))) {
            stock.setDeleteFlag(DeleteFlagStatus.TRUE.getCode());
        }
        stockService.updateById(stock);

        //新任务开始，待撞车发运
        WareShift newWareShift = BeanConverUtil.conver(wareShift, WareShift.class);
        newWareShift.setSsccNb(MesBarCodeUtil.getSSCC(splitPallet.getNewMesBarCode()));
        newWareShift.setStatus(KanbanStatusEnum.OUT_DOWN.value());
        newWareShift.setSourceSscc(splitPallet.getSourceSsccNb());
        this.save(newWareShift);

        //对新的sscc生成一个删除掉的库存
        Stock conver = BeanConverUtil.conver(stock, Stock.class);
        conver.setSsccNumber(MesBarCodeUtil.getSSCC(splitPallet.getNewMesBarCode()));
        conver.setTotalStock(splitPallet.getSplitQuantity());
        conver.setUpdateTime(new Date());
        conver.setUpdateBy(SecurityUtils.getUsername());
        conver.setId(null);
        conver.setDeleteFlag(DeleteFlagStatus.TRUE.getCode());
        stockService.save(conver);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchPerformBinIn(WareShiftBatchBinInDTO dto) {
        LambdaQueryWrapper<TranshipmentOrder> qw = new LambdaQueryWrapper<>();
        qw.eq(TranshipmentOrder::getSsccNumber, MesBarCodeUtil.getSSCC(dto.getMesBarCode()));
        qw.eq(TranshipmentOrder::getStatus, StatusEnums.TRUE.getCode());
        qw.eq(TranshipmentOrder::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        qw.orderByDesc(TranshipmentOrder::getCreateTime);
        qw.last("limit 1");
        TranshipmentOrder transhipmentOrder = transhipmentOrderService.getOne(qw);
        if (transhipmentOrder.getStatus() == 0) {
            throw new ServiceException("当前车次货物还没有收货");
        }
        LambdaQueryWrapper<TranshipmentOrder> tqw = new LambdaQueryWrapper<>();
        tqw.eq(TranshipmentOrder::getOrderNumber, transhipmentOrder.getOrderNumber());
        tqw.eq(TranshipmentOrder::getStatus, StatusEnums.TRUE.getCode());
        tqw.eq(TranshipmentOrder::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        List<TranshipmentOrder> ssccByOrder = transhipmentOrderService.list(tqw);
        if (CollectionUtils.isEmpty(ssccByOrder)) {
            throw new ServiceException("当前车次没有待上架的信息");
        }
        List<String> ssccList = ssccByOrder.stream().map(TranshipmentOrder::getSsccNumber).collect(Collectors.toList());

        LambdaQueryWrapper<WareShift> wareShiftQueryWrapper = new LambdaQueryWrapper<>();
        wareShiftQueryWrapper.in(WareShift::getSsccNb, ssccList);
        wareShiftQueryWrapper.eq(WareShift::getStatus, KanbanStatusEnum.INNER_BIN_IN.value());
        wareShiftQueryWrapper.eq(WareShift::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        List<WareShift> wareShiftList = this.list(wareShiftQueryWrapper);
        List<BinIn> binInsInsertList = new ArrayList<>();


        LambdaQueryWrapper<BinIn> binInQueryWrapper = new LambdaQueryWrapper<>();
        binInQueryWrapper.in(BinIn::getSsccNumber, ssccList);
        binInQueryWrapper.eq(BinIn::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        List<BinIn> list = binInService.list(binInQueryWrapper);
        if (!CollectionUtils.isEmpty(list)) {
            list.stream().forEach(item -> item.setDeleteFlag(DeleteFlagStatus.TRUE.getCode()));
            binInService.updateBatchById(list);
        }


        List<Stock> stockList = new ArrayList<>();
        List<UserOperationLog> operationLogs = new ArrayList<>();
        wareShiftList.stream().forEach(item -> {
            BinIn binIn = new BinIn();
            binIn.setSsccNumber(item.getSsccNb());
            binIn.setQuantity(item.getQuantity());
            binIn.setMaterialNb(item.getMaterialNb());
            binIn.setBatchNb(item.getBatchNb());
            binIn.setExpireDate(item.getExpireDate());
            binIn.setAreaCode(dto.getAreaCode());
            binIn.setWareCode(SecurityUtils.getWareCode());
            binIn.setStatus(BinInStatusEnum.FINISH.value());
            binIn.setMoveType(MoveTypeEnums.SPLIT_IN.getCode());
            binIn.setPlantNb(binInService.getWareInfo(SecurityUtils.getWareCode()).getFactoryCode());
            binInsInsertList.add(binIn);


            //插入库存

            Stock lastOneBySSCC = new Stock();
            if (item.getSplitType() == 1) {
                lastOneBySSCC = stockService.getOneStock(item.getSourceSscc());
            } else {
                StockVO oneBySSCC = stockService.getLastOneBySSCC(item.getSsccNb());
                lastOneBySSCC = BeanConverUtil.conver(oneBySSCC, Stock.class);
            }

            Stock stock = new Stock();
            stock.setPlantNb(binIn.getPlantNb());
            stock.setWareCode(SecurityUtils.getWareCode());
            stock.setSsccNumber(binIn.getSsccNumber());
            stock.setWareCode(binIn.getWareCode());
            stock.setBinCode(binIn.getActualBinCode());
            stock.setFrameCode(binIn.getActualFrameCode());
            stock.setMaterialNb(binIn.getMaterialNb());
            stock.setBatchNb(binIn.getBatchNb());
            stock.setExpireDate(binIn.getExpireDate());
            stock.setTotalStock(binIn.getQuantity());
            stock.setFreezeStock(Double.valueOf(0));
            stock.setAvailableStock(stock.getTotalStock() - stock.getFreezeStock());
            stock.setBinInId(binIn.getId());

            stock.setCreateBy(SecurityUtils.getUsername());
            stock.setCreateTime(new Date());
            stock.setQualityStatus(StringUtils.isEmpty(lastOneBySSCC.getQualityStatus()) ? QualityStatusEnums.WAITING_QUALITY.getCode() : lastOneBySSCC.getQualityStatus());
            stock.setFromPurchaseOrder(binIn.getFromPurchaseOrder());
            stock.setAreaCode(binIn.getAreaCode());
            stock.setPalletCode(binIn.getPalletCode());

            stockList.add(stock);

            item.setStatus(KanbanStatusEnum.FINISH.value());

            UserOperationLog userOperationLog = new UserOperationLog();
            userOperationLog.setCode(stock.getMaterialNb());
            userOperationLog.setSsccNumber(stock.getSsccNumber());
            operationLogs.add(userOperationLog);

        });


        binInService.saveBatch(binInsInsertList);

        stockService.saveBatch(stockList);

        this.updateBatchById(wareShiftList);

        operationLogService.insertUserOperationLog(MaterialType.MATERIAL.getCode(), null, SecurityUtils.getUsername(), UserOperationType.SHIFT_BININ.getCode(), operationLogs);

    }

    private List<WareShift> gennerateWareShift(String materialNb, Double shiftQuality, String ids, String orders) {
        LambdaQueryWrapper<Stock> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Stock::getMaterialNb, materialNb);
        lambdaQueryWrapper.eq(Stock::getQualityStatus, QualityStatusEnums.USE.getCode());
        lambdaQueryWrapper.eq(Stock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        lambdaQueryWrapper.eq(Stock::getFreezeStock, Double.valueOf(0));
        lambdaQueryWrapper.ge(Stock::getExpireDate, new Date());
        lambdaQueryWrapper.notIn(Stock::getAreaCode, AreaListConstants.mainAreaList);
        List<Stock> stockList = stockService.list(lambdaQueryWrapper);
        if (CollectionUtils.isEmpty(stockList)) {
            throw new ServiceException("没有该物料" + materialNb + "对应的外库库存");
        }
        List<Stock> sortedStockList = new ArrayList<>();
        sortedStockList =
                stockList.stream().filter(item -> item.getAvailableStock() != 0
                        && !AreaListConstants.mainArea(item.getAreaCode())
                        && !AreaListConstants.noQualifiedArea(item.getAreaCode())).
                        sorted(Comparator.comparing(Stock::getExpireDate).thenComparing(Stock::getWholeFlag, Comparator.reverseOrder())).collect(Collectors.toList());
        double count = 0;
        List<Stock> useMaterialStockList = new ArrayList<>();

        for (Stock stock : sortedStockList) {
            count = DoubleMathUtil.doubleMathCalculation(count, stock.getAvailableStock(), "+");
            useMaterialStockList.add(stock);
            if (count >= shiftQuality) {
                break;
            }
        }
        boolean splitFlag = false;
        if (count > shiftQuality) {
            //查询物料主数据，看是不是要整托移库的

            splitFlag = true;

        }
        R<MaterialVO> materialVORes = remoteMaterialService.getInfoByMaterialCode(materialNb);
        if (StringUtils.isNull(materialVORes) || Objects.isNull(materialVORes.getData())) {
            throw new ServiceException("该物料：" + materialNb + " 不存在");
        }

        if (R.FAIL == materialVORes.getCode()) {
            throw new ServiceException(materialVORes.getMsg());
        }
        MaterialVO materialVO = materialVORes.getData();
        if ("Y".equals(materialVO.getWholeShiftFlag())) {
            splitFlag = false;
        }

        List<WareShift> wareShiftList = new ArrayList<>();
        useMaterialStockList.stream().forEach(item -> {
            item.setFreezeStock(item.getTotalStock());
            WareShift wareShift = WareShift.builder().sourcePlantNb(item.getPlantNb()).sourceWareCode(item.getWareCode()).sourceAreaCode(item.getAreaCode())
                    .sourceBinCode(item.getBinCode()).materialNb(item.getMaterialNb()).expireDate(item.getExpireDate()).batchNb(item.getBatchNb())
                    .ssccNb(item.getSsccNumber()).deleteFlag(DeleteFlagStatus.FALSE.getCode()).moveType(MoveTypeEnums.WARE_SHIFT.getCode())
                    .status(KanbanStatusEnum.WAITING_BIN_DOWN.value())
                    .quantity(item.getTotalStock())
                    .callId(ids)
                    .orderNumber(orders)
                    .type(WareShiftTypeEnum.PICK.code())
                    .build();
            wareShiftList.add(wareShift);
        });
        //如果最后一托是拆托
        if (splitFlag) {
            WareShift wareShift = wareShiftList.get(wareShiftList.size() - 1);
            wareShift.setSplitType(1);
            wareShift.setSplitQuality(wareShift.getQuantity() - (count - shiftQuality));
        }
        stockService.updateBatchById(useMaterialStockList);

        return CollectionUtils.isEmpty(wareShiftList) ? Collections.emptyList() : wareShiftList;
    }

    private List<WareShift> getWareShiftByCall(Long callId, String materialNb, Double shiftQuality) {
        LambdaQueryWrapper<Stock> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Stock::getMaterialNb, materialNb);
        lambdaQueryWrapper.eq(Stock::getQualityStatus, QualityStatusEnums.USE.getCode());
        lambdaQueryWrapper.eq(Stock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        lambdaQueryWrapper.eq(Stock::getFreezeStock, Double.valueOf(0));
        lambdaQueryWrapper.ge(Stock::getExpireDate, new Date());
        lambdaQueryWrapper.notIn(Stock::getAreaCode, AreaListConstants.mainAreaList);
        List<Stock> stockList = stockService.list(lambdaQueryWrapper);
        if (CollectionUtils.isEmpty(stockList)) {
            throw new ServiceException("没有该物料" + materialNb + "对应的外库库存");
        }
        List<Stock> sortedStockList = new ArrayList<>();
        sortedStockList =
                stockList.stream().filter(item -> item.getAvailableStock() != 0).
                        sorted(Comparator.comparing(Stock::getExpireDate).thenComparing(Stock::getWholeFlag, Comparator.reverseOrder())).collect(Collectors.toList());
        double count = 0;
        List<Stock> useMaterialStockList = new ArrayList<>();

        for (Stock stock : sortedStockList) {
            count = DoubleMathUtil.doubleMathCalculation(count, stock.getAvailableStock(), "+");
            useMaterialStockList.add(stock);
            if (count >= shiftQuality) {
                break;
            }
        }
        boolean splitFlag = false;
        if (count > shiftQuality) {
            //查询物料主数据，看是不是要整托移库的

            splitFlag = true;

        }
        R<MaterialVO> materialVORes = remoteMaterialService.getInfoByMaterialCode(materialNb);
        if (StringUtils.isNull(materialVORes) || Objects.isNull(materialVORes.getData())) {
            throw new ServiceException("该物料：" + materialNb + " 不存在");
        }

        if (R.FAIL == materialVORes.getCode()) {
            throw new ServiceException(materialVORes.getMsg());
        }
        MaterialVO materialVO = materialVORes.getData();
        if ("Y".equals(materialVO.getWholeShiftFlag())) {
            splitFlag = false;
        }

        List<WareShift> wareShiftList = new ArrayList<>();
        useMaterialStockList.stream().forEach(item -> {
            item.setFreezeStock(item.getTotalStock());
            WareShift wareShift = WareShift.builder().sourcePlantNb(item.getPlantNb()).sourceWareCode(item.getWareCode()).sourceAreaCode(item.getAreaCode())
                    .sourceBinCode(item.getBinCode()).materialNb(item.getMaterialNb()).expireDate(item.getExpireDate()).batchNb(item.getBatchNb())
                    .ssccNb(item.getSsccNumber()).deleteFlag(DeleteFlagStatus.FALSE.getCode()).moveType(MoveTypeEnums.WARE_SHIFT.getCode())
                    .status(KanbanStatusEnum.WAITING_BIN_DOWN.value())
                    .quantity(item.getTotalStock())
                    .callId(callId.toString())
                    .build();
            wareShiftList.add(wareShift);
        });
        //如果最后一托是拆托
        if (splitFlag) {
            WareShift wareShift = wareShiftList.get(wareShiftList.size() - 1);
            wareShift.setSplitType(1);
            wareShift.setSplitQuality(wareShift.getQuantity() - (count - shiftQuality));
        }
        stockService.updateBatchById(useMaterialStockList);

        return CollectionUtils.isEmpty(wareShiftList) ? Collections.emptyList() : wareShiftList;
    }


    @Override
    public int updateStatusByStatus(List<String> ssccs, Integer queryStatus, Integer status) {
        ArrayList<Object> ssccList = new ArrayList<>();
        ssccList.addAll(ssccs);

        if (queryStatus.equals(KanbanStatusEnum.INNER_RECEIVING.value())) {

            //移库入库完后，看是不是IQC任务，如果是IQC任务，把IQC任务状态修改为待抽样
            LambdaQueryWrapper<IQCSamplePlan> iqcQueryWrapper = new LambdaQueryWrapper<>();
            iqcQueryWrapper.in(IQCSamplePlan::getSsccNb, ssccList);
            iqcQueryWrapper.eq(IQCSamplePlan::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
            iqcQueryWrapper.ne(IQCSamplePlan::getStatus, IQCStatusEnum.FINISH.code());
            iqcQueryWrapper.ne(IQCSamplePlan::getStatus, IQCStatusEnum.CANCEL.code());

            List<IQCSamplePlan> samplePlanList = samplePlanService.list(iqcQueryWrapper);
            if (!CollectionUtils.isEmpty(samplePlanList)) {


                samplePlanList.stream().forEach(samplePlan -> {
                    //直接上架到IQC虚拟区域
                    StockVO lastOneBySSCC = stockService.getLastOneBySSCC(samplePlan.getSsccNb());
                    String mesBarCode = MesBarCodeUtil.generateMesBarCode(lastOneBySSCC.getExpireDate(), lastOneBySSCC.getSsccNumber(), lastOneBySSCC.getMaterialNb(), lastOneBySSCC.getBatchNb(), lastOneBySSCC.getTotalStock());
                    BinInVO binInVO = binInService.performToAreaType(mesBarCode, lastOneBySSCC.getTotalStock(), AreaTypeEnum.IQC.getCode());
                    //修改IQC抽样状态
                    samplePlan.setStatus(IQCStatusEnum.WAITING_SAMPLE.code());
                    samplePlan.setWareCode(SecurityUtils.getWareCode());
                    samplePlan.setBinDownCode(binInVO.getActualBinCode());
                    samplePlan.setPlantNb(binInVO.getPlantNb());
                });
                samplePlanService.updateBatchById(samplePlanList);

                List<String> iqcSSCCList = samplePlanList.stream().map(IQCSamplePlan::getSsccNb).collect(Collectors.toList());
                //属于IQC的移库，更新为完成状态
                WareShift wareShiftIQC = new WareShift();
                wareShiftIQC.setStatus(KanbanStatusEnum.FINISH.value());
                wareShiftIQC.setTargetWareCode(SecurityUtils.getWareCode());
                LambdaUpdateWrapper<WareShift> uwIQC = new LambdaUpdateWrapper<>();
                uwIQC.in(WareShift::getSsccNb, iqcSSCCList);
                uwIQC.eq(WareShift::getStatus, queryStatus);
                uwIQC.eq(WareShift::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
                uwIQC.ne(WareShift::getStatus, KanbanStatusEnum.FINISH.value());
                uwIQC.ne(WareShift::getStatus, KanbanStatusEnum.CANCEL.value());
                uwIQC.ne(WareShift::getStatus, KanbanStatusEnum.LINE_RECEIVED.value());

//                uwIQC.eq(WareShift::getTargetWareCode, SecurityUtils.getWareCode());
                wareShiftMapper.update(wareShiftIQC, uwIQC);

                //  其余的更新为待上架

                ssccList.removeAll(iqcSSCCList);
            }
        }
        if (CollectionUtils.isEmpty(ssccList)) {
            return 0;
        }
        WareShift wareShift = new WareShift();
        wareShift.setStatus(status);
        wareShift.setTargetWareCode(SecurityUtils.getWareCode());
        LambdaUpdateWrapper<WareShift> uw = new LambdaUpdateWrapper<>();
        uw.in(WareShift::getSsccNb, ssccList);
        uw.eq(WareShift::getStatus, queryStatus);
        uw.eq(WareShift::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
//        uw.eq(WareShift::getTargetWareCode, SecurityUtils.getWareCode());


        return wareShiftMapper.update(wareShift, uw);
    }
}
