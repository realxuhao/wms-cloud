package com.bosch.binin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.binin.api.domain.IQCSamplePlan;
import com.bosch.binin.api.domain.MaterialKanban;
import com.bosch.binin.api.domain.Stock;
import com.bosch.binin.api.domain.WareShift;
import com.bosch.binin.api.domain.dto.AddShiftTaskDTO;
import com.bosch.binin.api.domain.dto.BinInDTO;
import com.bosch.binin.api.domain.dto.WareShiftQueryDTO;
import com.bosch.binin.api.domain.vo.BinInVO;
import com.bosch.binin.api.domain.vo.WareShiftVO;
import com.bosch.binin.api.enumeration.IQCStatusEnum;
import com.bosch.binin.api.enumeration.KanbanStatusEnum;
import com.bosch.binin.mapper.MaterialKanbanMapper;
import com.bosch.binin.mapper.WareShiftMapper;
import com.bosch.binin.service.IBinInService;
import com.bosch.binin.service.IMaterialKanbanService;
import com.bosch.binin.service.IWareShiftService;
import com.bosch.binin.service.IStockService;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.enums.MoveTypeEnums;
import com.ruoyi.common.core.enums.QualityStatusEnums;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.DoubleMathUtil;
import com.ruoyi.common.core.utils.MesBarCodeUtil;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
        wareShiftLambdaQueryWrapper.last("for update");
        WareShift wareShift = wareShiftMapper.selectOne(wareShiftLambdaQueryWrapper);
        if (Objects.isNull(wareShift)) {
            throw new ServiceException("该SSCC码 " + ssccNb + " 不存在移库任务");
        }
        if (!KanbanStatusEnum.WAITING_BIN_DOWN.value().equals(wareShift.getStatus())) {
            throw new ServiceException("该SSCC码 " + ssccNb + "对应任务状态为: " + KanbanStatusEnum.getDesc(String.valueOf(wareShift.getStatus())) + " 不可下架 ");

        }

        //在kanban任务中查询
        LambdaQueryWrapper<MaterialKanban> kanbanQueryWrapper = new LambdaQueryWrapper<>();
        kanbanQueryWrapper.eq(MaterialKanban::getSsccNumber, ssccNb);
        kanbanQueryWrapper.eq(MaterialKanban::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        kanbanQueryWrapper.eq(MaterialKanban::getStatus, KanbanStatusEnum.WAITING_BIN_DOWN.value());
        MaterialKanban materialKanban = kanbanMapper.selectOne(kanbanQueryWrapper);
        //kanban任务修改状态
        if (materialKanban != null) {
            materialKanban.setStatus(KanbanStatusEnum.OUT_DOWN.value());
            kanbanMapper.updateById(materialKanban);
        }

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
        if (KanbanStatusEnum.WAITING_BIN_DOWN.value().equals(wareShift.getStatus())) {
            //如果不是空，走主库上架
            if (StringUtils.isNotEmpty(wareShift.getTargetWareCode())) {
                binInService.generateInTaskByOldStock(wareShift.getSsccNb(), Double.valueOf(0), wareShift.getTargetWareCode());

            } else {
                Stock stock = stockService.getAvailablesStockBySscc(wareShift.getSsccNb());
                if (stock != null) {
                    stock.setAvailableStock(stock.getTotalStock());
                    stock.setFreezeStock(Double.valueOf(0));
                    stockService.updateById(stock);
                }
            }
        }
        //外库待发运，取消后直接在外库上架
        if (KanbanStatusEnum.OUT_DOWN.value().equals(wareShift.getStatus())) {
            binInService.generateInTaskByOldStock(wareShift.getSsccNb(), Double.valueOf(0), wareShift.getSourceWareCode());

        }
        //待上架状态，直接在收货仓库上架
//        if (KanbanStatusEnum.INNER_BIN_IN.value().equals(wareShift.getStatus())) {
//            binInService.generateInTaskByOldStock(wareShift.getSsccNb(), Double.valueOf(0), wareShift.getTargetWareCode());
//        }

        //如果kanban有，需要对应更新为取消
        MaterialKanban kanban = kanbanService.getOneBySCAndStatus(wareShift.getSsccNb(), KanbanStatusEnum.WAITING_ISSUE.value());
        if (kanban != null) {
            kanban.setStatus(KanbanStatusEnum.CANCEL.value());
            kanbanService.updateById(kanban);
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
            wareShift.setStatus(KanbanStatusEnum.WAITING_BIN_DOWN.value());
            wareShiftList.add(wareShift);

            item.setFreezeStock(item.getTotalStock());
            item.setAvailableStock(DoubleMathUtil.doubleMathCalculation(item.getTotalStock(), item.getAvailableStock(), "-"));
        });
        stockService.updateBatchById(stockList);
        return saveBatch(wareShiftList);
    }

    @Override
    public void performBinIn(BinInDTO binInDTO) {

        String mesBarCode = binInDTO.getMesBarCode();
        String sscc = MesBarCodeUtil.getSSCC(mesBarCode);
        LambdaQueryWrapper<WareShift> iqcQueryWrapper = new LambdaQueryWrapper<>();
        iqcQueryWrapper.eq(WareShift::getSsccNb, sscc);
        iqcQueryWrapper.eq(WareShift::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        WareShift wareShift = wareShiftMapper.selectOne(iqcQueryWrapper);
        if (wareShift == null) {
            throw new ServiceException("没有该sscc:" + sscc + "对应的移库待上架任务");
        }
        if (wareShift.getStatus() != KanbanStatusEnum.INNER_BIN_IN.value()) {
            throw new ServiceException("sscc:" + sscc + "对应任务状态为:" + KanbanStatusEnum.getDesc(String.valueOf(wareShift.getStatus())) + ",不可上架");
        }
        BinInVO binInVO = binInService.performBinIn(binInDTO);
        //更新移库任务
        wareShift.setStatus(KanbanStatusEnum.FINISH.value());
        wareShift.setTargetPlant(binInVO.getPlantNb());
        wareShift.setTargetWareCode(binInVO.getWareCode());
        wareShift.setTargetAreaCode(binInVO.getAreaCode());
        wareShift.setTargetBinCode(binInVO.getActualBinCode());
        wareShiftMapper.updateById(wareShift);

        //如果是看板所产生的移库任务，看板任务也要随之更新
        //如果是kanban任务
        LambdaQueryWrapper<MaterialKanban> kanbanQueryWrapper = new LambdaQueryWrapper<>();
        //待下架任务,该kanban状态，待下架
        kanbanQueryWrapper.eq(MaterialKanban::getSsccNumber, sscc);
        kanbanQueryWrapper.eq(MaterialKanban::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        kanbanQueryWrapper.ne(MaterialKanban::getStatus, KanbanStatusEnum.FINISH.value());
        kanbanQueryWrapper.last("limit 1");
        kanbanQueryWrapper.last("for update");
        MaterialKanban materialKanban = kanbanMapper.selectOne(kanbanQueryWrapper);
        if (!Objects.isNull(materialKanban)) {
            materialKanban.setStatus(KanbanStatusEnum.WAITING_BIN_DOWN.value());
            materialKanban.setFactoryCode(binInVO.getPlantNb());
            materialKanban.setWareCode(binInVO.getWareCode());
            materialKanban.setAreaCode(binInVO.getAreaCode());
            materialKanban.setBinCode(binInVO.getActualBinCode());
            kanbanMapper.updateById(materialKanban);
            //需要冻结库存
            LambdaQueryWrapper<Stock> stockQw = new LambdaQueryWrapper<>();
            stockQw.eq(Stock::getSsccNumber, sscc);
            stockQw.eq(Stock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
            Stock stock = stockService.getOne(stockQw);

            stock.setFreezeStock(materialKanban.getQuantity());
            stock.setAvailableStock(DoubleMathUtil.doubleMathCalculation(stock.getTotalStock(), stock.getFreezeStock(), "-"));
            stockService.updateById(stock);

        }
    }

    @Override
    public int updateStatusByStatus(List<String> ssccs, Integer queryStatus, Integer status) {
        WareShift wareShift = new WareShift();
        wareShift.setStatus(status);
        wareShift.setTargetWareCode(SecurityUtils.getWareCode());
        LambdaUpdateWrapper<WareShift> uw = new LambdaUpdateWrapper<>();
        uw.in(WareShift::getSsccNb, ssccs);
        uw.eq(WareShift::getStatus, queryStatus);
        uw.eq(WareShift::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
//        uw.eq(WareShift::getTargetWareCode, SecurityUtils.getWareCode());

        return wareShiftMapper.update(wareShift, uw);
    }
}
