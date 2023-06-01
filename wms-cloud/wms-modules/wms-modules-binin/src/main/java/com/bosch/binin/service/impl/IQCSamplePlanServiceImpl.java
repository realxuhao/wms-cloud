package com.bosch.binin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.binin.api.domain.BinIn;
import com.bosch.binin.api.domain.IQCSamplePlan;
import com.bosch.binin.api.domain.Stock;
import com.bosch.binin.api.domain.WareShift;
import com.bosch.binin.api.domain.dto.BinInDTO;
import com.bosch.binin.api.domain.dto.IQCSamplePlanDTO;
import com.bosch.binin.api.domain.dto.IQCSamplePlanQueryDTO;
import com.bosch.binin.api.domain.dto.IQCWareShiftDTO;
import com.bosch.binin.api.domain.vo.BinInVO;
import com.bosch.binin.api.domain.vo.IQCSamplePlanVO;
import com.bosch.binin.api.enumeration.IQCStatusEnum;
import com.bosch.binin.api.enumeration.KanbanStatusEnum;
import com.bosch.binin.mapper.IQCSamplePlanMapper;
import com.bosch.binin.service.IBinInService;
import com.bosch.binin.service.IIQCSamplePlanService;
import com.bosch.binin.service.IStockService;
import com.bosch.binin.service.IWareShiftService;
import com.bosch.masterdata.api.RemoteMaterialService;
import com.bosch.masterdata.api.domain.dto.MaterialDTO;
import com.bosch.masterdata.api.domain.vo.MaterialVO;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.ruoyi.common.core.constant.AreaListConstants;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.enums.MoveTypeEnums;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.MesBarCodeUtil;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-02-07 16:15
 **/
@Service
public class IQCSamplePlanServiceImpl extends ServiceImpl<IQCSamplePlanMapper, IQCSamplePlan> implements IIQCSamplePlanService {

    @Autowired
    private IQCSamplePlanMapper samplePlanMapper;

    @Autowired
    private IStockService stockService;

    @Autowired
    private IBinInService binInService;

    @Autowired
    private RemoteMaterialService remoteMaterialService;

    @Autowired
    private IWareShiftService wareShiftService;

    @Override
    public List<IQCSamplePlanVO> getSamplePlan(IQCSamplePlanQueryDTO dto) {
        List<IQCSamplePlanVO> samplePlanList = samplePlanMapper.getSamplePlanList(dto);
        if (CollectionUtils.isEmpty(samplePlanList)) {
            return Collections.emptyList();
        }
        return samplePlanList;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void manualAdd(List<IQCSamplePlanDTO> dto) {
        if (Objects.isNull(dto)) {
            throw new ServiceException("请选中数据后重试");
        }

        List<String> ssccList = dto.stream().map(IQCSamplePlanDTO::getSsccNb).collect(Collectors.toList());
        Map<String, IQCSamplePlanDTO> ssccMaps = dto.stream().collect(Collectors.toMap(IQCSamplePlanDTO::getSsccNb, Function.identity()));


        LambdaQueryWrapper<Stock> stockQueryWrapper = new LambdaQueryWrapper<>();
        stockQueryWrapper.in(Stock::getSsccNumber, ssccList)
                .eq(Stock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode())
                .eq(Stock::getFreezeStock, (double) 0);
        List<Stock> stockList = stockService.list(stockQueryWrapper);
        if (Objects.isNull(stockList) || stockList.size() != ssccList.size()) {
            throw new ServiceException("库存数据过期，请刷新后重试");
        }

        List<String> materialNbList = stockList.stream().map(Stock::getMaterialNb).collect(Collectors.toList());
        MaterialDTO materialDTO = new MaterialDTO();
        materialDTO.setMaterialNbList(materialNbList);
        materialDTO.setPageSize(0);
        R<PageVO<MaterialVO>> R = remoteMaterialService.list(materialDTO);
        Map<String, MaterialVO> materialVOMap = new HashMap<>();
        if (R == null || !R.isSuccess()) {
            throw new ServiceException("不存在该物料");
        }
        List<MaterialVO> materialVOS = R.getData().getRows();
        materialVOMap = materialVOS.stream().collect(Collectors.toMap(MaterialVO::getCode, Function.identity()));


        List<IQCSamplePlan> samplePlanList = new ArrayList();
        Map<String, MaterialVO> finalMaterialVOMap = materialVOMap;
        List<WareShift> wareShiftList = new ArrayList<>();
        stockList.stream().forEach(stock -> {
            if (stock.getTotalStock() < ssccMaps.get(stock.getSsccNumber()).getSampleQuantity()) {
                throw new ServiceException("抽样数量不能大于库存数量，请重试");
            }
            IQCSamplePlan iqcSamplePlan = new IQCSamplePlan();
            iqcSamplePlan.setSsccNb(stock.getSsccNumber());
            iqcSamplePlan.setCell(finalMaterialVOMap.get(stock.getMaterialNb()).getCell());
            iqcSamplePlan.setMaterialNb(stock.getMaterialNb());
            iqcSamplePlan.setBinDownCode(stock.getBinCode());
//            iqcSamplePlan.setBinDownTime(new Date());
            iqcSamplePlan.setRecommendSampleQuantity(ssccMaps.get(stock.getSsccNumber()).getSampleQuantity());
//            iqcSamplePlan.setStatus(stock.getPlantNb().equals("7751") ? IQCStatusEnum.WAITING_BIN_DOWN.code() : IQCStatusEnum.WARE_SHIFTING.code());
            iqcSamplePlan.setStatus(IQCStatusEnum.WAAITTING_ISSUE.code());
            iqcSamplePlan.setBatchNb(stock.getBatchNb());
            iqcSamplePlan.setWareCode(stock.getWareCode());
            iqcSamplePlan.setExpireDate(stock.getExpireDate());
            iqcSamplePlan.setQuantity(stock.getTotalStock());
            iqcSamplePlan.setPlantNb(stock.getPlantNb());
            iqcSamplePlan.setAreaCode(stock.getAreaCode());
            //
            MaterialVO materialVO = binInService.getMaterialVOByCode(stock.getMaterialNb());
            iqcSamplePlan.setUnit(materialVO.getUnit());

            samplePlanList.add(iqcSamplePlan);
            //库存冻结
            stock.setFreezeStock(stock.getAvailableStock());
            stock.setAvailableStock(stock.getTotalStock() - stock.getFreezeStock());
            //下发的时候新增
            //如果是外库的，新增移库
//            if (stock.getPlantNb().equals("7752")) {
//                WareShift wareShift = WareShift.builder().sourcePlantNb(stock.getPlantNb()).sourceWareCode(stock.getWareCode()).sourceAreaCode(stock.getAreaCode())
//                        .sourceBinCode(stock.getBinCode()).materialNb(stock.getMaterialNb()).batchNb(stock.getBatchNb()).expireDate(stock.getExpireDate())
//                        .ssccNb(stock.getSsccNumber()).deleteFlag(DeleteFlagStatus.FALSE.getCode()).moveType(MoveTypeEnums.WARE_SHIFT.getCode())
//                        .status(KanbanStatusEnum.WAITING_BIN_DOWN.value())
//                        .quantity(stock.getTotalStock())
//                        .build();
//                wareShiftList.add(wareShift);
//            }


        });

        saveBatch(samplePlanList);

        stockService.updateBatchById(stockList);

        wareShiftService.saveBatch(wareShiftList);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifySscc(IQCSamplePlanDTO dto) {
        if (Objects.isNull(dto) || StringUtils.isNull(dto.getSourceSsccNb())) {
            throw new ServiceException("请选中数据后重试");
        }
        LambdaQueryWrapper<IQCSamplePlan> iqcQueryWrapper = new LambdaQueryWrapper<>();
        iqcQueryWrapper.eq(IQCSamplePlan::getSsccNb, dto.getSourceSsccNb());
        iqcQueryWrapper.eq(IQCSamplePlan::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        iqcQueryWrapper.ne(IQCSamplePlan::getStatus,IQCStatusEnum.FINISH.code());
        iqcQueryWrapper.ne(IQCSamplePlan::getStatus,IQCStatusEnum.CANCEL.code());

        IQCSamplePlan iqcSamplePlan = samplePlanMapper.selectOne(iqcQueryWrapper);
        if (iqcSamplePlan == null) {
            throw new ServiceException("sscc:" + dto.getSourceSsccNb() + "对应的抽样信息不存在");
        }

        if (!(iqcSamplePlan.getStatus() == IQCStatusEnum.WAAITTING_ISSUE.code() || iqcSamplePlan.getStatus() == IQCStatusEnum.WAITING_SAMPLE.code() || iqcSamplePlan.getStatus() == IQCStatusEnum.WARE_SHIFTING.code())) {
            throw new ServiceException("状态为：" + IQCStatusEnum.getDesc(iqcSamplePlan.getStatus()) + ",不可以修改");
        }


        LambdaQueryWrapper<Stock> stockQueryWrapper = new LambdaQueryWrapper<>();
        stockQueryWrapper.eq(Stock::getSsccNumber, dto.getSsccNb()).eq(Stock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        Stock stock = stockService.getOne(stockQueryWrapper);
        if (!iqcSamplePlan.getMaterialNb().equals(stock.getMaterialNb())) {
            throw new ServiceException("只能修改为同一物料号的库存sscc");
        }
        if (Objects.isNull(stock) || stock.getFreezeStock() > 0) {
            throw new ServiceException("库存状态过期，请重新选择");
        }
        if (dto.getSampleQuantity() > stock.getTotalStock()) {
            throw new ServiceException("抽样数量不能大于库存数量");
        }
        if (!AreaListConstants.mainAreaList.contains(stock.getAreaCode())) {
            List<WareShift> wareShiftList = new ArrayList<>();

            //老的移库任务取消
            LambdaQueryWrapper<WareShift> wareShiftQueryWrapper = new LambdaQueryWrapper<>();
            wareShiftQueryWrapper.eq(WareShift::getSsccNb, dto.getSourceSsccNb()).eq(WareShift::getDeleteFlag, DeleteFlagStatus.FALSE.getCode()).ne(WareShift::getStatus,KanbanStatusEnum.CANCEL.value()).ne(WareShift::getStatus,KanbanStatusEnum.FINISH.value()).ne(WareShift::getStatus,KanbanStatusEnum.LINE_RECEIVED.value() );
            WareShift shift = wareShiftService.getOne(wareShiftQueryWrapper);
            if (shift == null) {
                return;
            }
            if (shift != null && !(shift.getStatus().equals(KanbanStatusEnum.WAITING_BIN_DOWN.value())
                    || shift.getStatus().equals(KanbanStatusEnum.CANCEL.value()))) {
                throw new ServiceException("对应移库任务状态为" + KanbanStatusEnum.getDesc(String.valueOf(shift.getStatus())) + ",不可修改");
            }
            shift.setStatus(KanbanStatusEnum.CANCEL.value());
            wareShiftService.updateById(shift);
            //如果是外库的，新增移库
            WareShift wareShift = WareShift.builder().sourcePlantNb(stock.getPlantNb()).sourceWareCode(stock.getWareCode()).sourceAreaCode(stock.getAreaCode())
                    .sourceBinCode(stock.getBinCode()).materialNb(stock.getMaterialNb()).batchNb(stock.getBatchNb()).expireDate(stock.getExpireDate())
                    .ssccNb(stock.getSsccNumber()).deleteFlag(DeleteFlagStatus.FALSE.getCode()).moveType(MoveTypeEnums.WARE_SHIFT.getCode())
                    .status(KanbanStatusEnum.WAITING_BIN_DOWN.value())
                    .quantity(stock.getTotalStock())
                    .build();
            wareShiftList.add(wareShift);

            wareShiftService.saveBatch(wareShiftList);


        }

        //老任务取消
        iqcSamplePlan.setStatus(IQCStatusEnum.CANCEL.code());
        updateById(iqcSamplePlan);

        //新建任务
        IQCSamplePlan samplePlan = new IQCSamplePlan();
        samplePlan.setSsccNb(dto.getSsccNb());
        samplePlan.setCell(iqcSamplePlan.getCell());
        samplePlan.setMaterialNb(iqcSamplePlan.getMaterialNb());
        samplePlan.setBinDownCode(stock.getBinCode());
        samplePlan.setBatchNb(stock.getBatchNb());
        samplePlan.setWareCode(stock.getWareCode());
        samplePlan.setPlantNb(stock.getPlantNb());
        samplePlan.setQuantity(stock.getTotalStock());


        samplePlan.setExpireDate(stock.getExpireDate());
        samplePlan.setBinDownTime(new Date());
        samplePlan.setRecommendSampleQuantity(dto.getSampleQuantity());
        samplePlan.setAreaCode(stock.getAreaCode());
        samplePlan.setStatus(AreaListConstants.mainAreaList.contains(stock.getAreaCode())? IQCStatusEnum.WAITING_BIN_DOWN.code() : IQCStatusEnum.WARE_SHIFTING.code());
        this.save(samplePlan);

        stock.setFreezeStock(stock.getAvailableStock());
        stock.setAvailableStock(stock.getTotalStock() - stock.getFreezeStock());
        stockService.updateById(stock);


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void binDown(String ssccNb) {
        LambdaQueryWrapper<IQCSamplePlan> iqcQueryWrapper = new LambdaQueryWrapper<>();
        iqcQueryWrapper.eq(IQCSamplePlan::getSsccNb, ssccNb);
        iqcQueryWrapper.eq(IQCSamplePlan::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        iqcQueryWrapper.ne(IQCSamplePlan::getStatus,IQCStatusEnum.CANCEL.code());
        iqcQueryWrapper.ne(IQCSamplePlan::getStatus,IQCStatusEnum.FINISH.code());

        IQCSamplePlan iqcSamplePlan = samplePlanMapper.selectOne(iqcQueryWrapper);
        if (iqcSamplePlan == null) {
            throw new ServiceException("没有该sscc:" + ssccNb + "对应的IQC抽样下架任务");
        }
        if (iqcSamplePlan.getStatus() != IQCStatusEnum.WAITING_BIN_DOWN.code()) {
            throw new ServiceException("sscc:" + ssccNb + "对应任务状态为:" + IQCStatusEnum.getDesc(iqcSamplePlan.getStatus()) + ",不可下架");
        }
        //删除库存
        LambdaQueryWrapper<Stock> stockQueryWrapper = new LambdaQueryWrapper<>();
        stockQueryWrapper.eq(Stock::getSsccNumber, ssccNb).eq(Stock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        Stock stock = stockService.getOne(stockQueryWrapper);
        if (stock == null) {
            throw new ServiceException("没有该sscc:" + ssccNb + "对应的库存数据");
        }
//        stock.setDeleteFlag(DeleteFlagStatus.TRUE.getCode());
//        stockService.updateById(stock);
//
        binInService.binDown(ssccNb);

        //更新IQC任务状态
        iqcSamplePlan.setStatus(IQCStatusEnum.WAITING_SAMPLE.code());
        //更新下架人下架时间
        iqcSamplePlan.setBinDownUser(SecurityUtils.getUsername());
        iqcSamplePlan.setBinDownTime(new Date());
        samplePlanMapper.updateById(iqcSamplePlan);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BinInVO getBinInInfo(String sscc) {

        LambdaQueryWrapper<IQCSamplePlan> iqcQueryWrapper = new LambdaQueryWrapper<>();
        iqcQueryWrapper.eq(IQCSamplePlan::getSsccNb, sscc);
        iqcQueryWrapper.eq(IQCSamplePlan::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        iqcQueryWrapper.ne(IQCSamplePlan::getStatus,IQCStatusEnum.CANCEL.code());
        iqcQueryWrapper.ne(IQCSamplePlan::getStatus,IQCStatusEnum.FINISH.code());

        IQCSamplePlan iqcSamplePlan = samplePlanMapper.selectOne(iqcQueryWrapper);
        if (iqcSamplePlan == null) {
            throw new ServiceException("没有该sscc:" + sscc + "对应的IQC抽样待上架任务");
        }
        if (iqcSamplePlan.getStatus() != IQCStatusEnum.WAITING_BIN_IN.code()) {
            throw new ServiceException("sscc:" + sscc + "对应任务状态为:" + IQCStatusEnum.getDesc(iqcSamplePlan.getStatus()) + ",不可上架");
        }


        BinInVO binInVO = binInService.generateInTaskByOldStock(sscc, iqcSamplePlan.getSampleQuantity(), SecurityUtils.getWareCode());

        return binInVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void performBinIn(BinInDTO binInDTO) {
        String mesBarCode = binInDTO.getMesBarCode();
        String sscc = MesBarCodeUtil.getSSCC(mesBarCode);
        LambdaQueryWrapper<IQCSamplePlan> iqcQueryWrapper = new LambdaQueryWrapper<>();
        iqcQueryWrapper.eq(IQCSamplePlan::getSsccNb, sscc);
        iqcQueryWrapper.eq(IQCSamplePlan::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        iqcQueryWrapper.ne(IQCSamplePlan::getStatus,IQCStatusEnum.CANCEL.code());
        iqcQueryWrapper.ne(IQCSamplePlan::getStatus,IQCStatusEnum.FINISH.code());

        IQCSamplePlan iqcSamplePlan = samplePlanMapper.selectOne(iqcQueryWrapper);
        if (iqcSamplePlan == null) {
            throw new ServiceException("没有该sscc:" + sscc + "对应的IQC抽样待上架任务");
        }
        if (iqcSamplePlan.getStatus() != IQCStatusEnum.WAITING_BIN_IN.code()) {
            throw new ServiceException("sscc:" + sscc + "对应任务状态为:" + IQCStatusEnum.getDesc(iqcSamplePlan.getStatus()) + ",不可上架");
        }
        //先把库存表之前的数据删除掉
        LambdaQueryWrapper<Stock> stockQueryWrapper = new LambdaQueryWrapper<>();
        stockQueryWrapper.eq(Stock::getSsccNumber,sscc);
        stockQueryWrapper.eq(Stock::getDeleteFlag,DeleteFlagStatus.FALSE.getCode());
        List<Stock> stockList = stockService.list(stockQueryWrapper);
        if(!CollectionUtils.isEmpty(stockList)) {
            stockList.stream().forEach(item -> item.setDeleteFlag(DeleteFlagStatus.TRUE.getCode()));
            stockService.updateBatchById(stockList);
        }


        LambdaQueryWrapper<BinIn> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper1.eq(BinIn::getSsccNumber, sscc).eq(BinIn::getDeleteFlag, 0);
        BinIn binIn = binInService.getOne(lambdaQueryWrapper1);



        //删除上架表的历史数据
//        LambdaQueryWrapper<BinIn> binInQueryWrapper = new LambdaQueryWrapper<>();
//        binInQueryWrapper.eq(BinIn::getSsccNumber,sscc);
//        binInQueryWrapper.eq(BinIn::getDeleteFlag,DeleteFlagStatus.FALSE.getCode());
//        List<BinIn> binInList = binInService.list(binInQueryWrapper);
//        if(!CollectionUtils.isEmpty(binInList)) {
//            binInList.stream().forEach(item -> item.setDeleteFlag(DeleteFlagStatus.TRUE.getCode()));
//            binInService.updateBatchById(binInList);
//        }

        //再去执行上架
        BinInVO binInVO = binInService.performBinIn(binInDTO,null);

        iqcSamplePlan.setBinInCode(binInVO.getActualBinCode());
        iqcSamplePlan.setBinInTime(new Date());
        iqcSamplePlan.setBinInUser(SecurityUtils.getUsername());
        iqcSamplePlan.setStatus(IQCStatusEnum.FINISH.code());
        samplePlanMapper.updateById(iqcSamplePlan);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancel(Long id) {
        IQCSamplePlan samplePlan = getById(id);
        if (samplePlan == null || DeleteFlagStatus.TRUE.getCode().equals(samplePlan.getDeleteFlag())) {
            throw new ServiceException("无该IQC抽样任务");
        }
        if (samplePlan.getStatus() == IQCStatusEnum.CANCEL.code()) {
            throw new ServiceException("该任务已经取消，不能再次取消");
        }
        if (!(samplePlan.getStatus() == IQCStatusEnum.WAAITTING_ISSUE.code() || samplePlan.getStatus() == IQCStatusEnum.WAITING_BIN_DOWN.code() || samplePlan.getStatus() == IQCStatusEnum.WARE_SHIFTING.code())) {
            throw new ServiceException("该任务状态为:" + IQCStatusEnum.getDesc(samplePlan.getStatus()) + ",不可取消");
        }
        if (!AreaListConstants.mainAreaList.contains(samplePlan.getAreaCode())) {
            //移库任务
            LambdaQueryWrapper<WareShift> shiftQueryWrapper = new LambdaQueryWrapper<>();
            shiftQueryWrapper.eq(WareShift::getSsccNb, samplePlan.getSsccNb());
            shiftQueryWrapper.eq(WareShift::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
            shiftQueryWrapper.ne(WareShift::getStatus, KanbanStatusEnum.CANCEL.value());
            shiftQueryWrapper.ne(WareShift::getStatus, KanbanStatusEnum.FINISH.value());


            shiftQueryWrapper.last("limit 1");
            WareShift wareShift = wareShiftService.getOne(shiftQueryWrapper);
            if (!Objects.isNull(wareShift)) {
                if (!wareShift.getStatus().equals(KanbanStatusEnum.WAITING_BIN_DOWN.value())) {
                    throw new ServiceException("对应移库任务状态为: " + KanbanStatusEnum.getDesc(String.valueOf(wareShift.getStatus())) + ",不可取消");
                }
                wareShift.setStatus(KanbanStatusEnum.CANCEL.value());
                wareShiftService.updateById(wareShift);
            }
        }
        samplePlan.setStatus(IQCStatusEnum.CANCEL.code());
        updateById(samplePlan);
        //库存
        LambdaQueryWrapper<Stock> stockQueryWrapper = new LambdaQueryWrapper<>();
        stockQueryWrapper.eq(Stock::getSsccNumber, samplePlan.getSsccNb());
        stockQueryWrapper.eq(Stock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        Stock stock = stockService.getOne(stockQueryWrapper);
        stock.setFreezeStock(Double.valueOf(0));
        stock.setAvailableStock(stock.getTotalStock());
        stockService.updateById(stock);


    }

    @Override
    public IQCSamplePlanVO info(String mesBarCode) {
        IQCSamplePlanQueryDTO dto = new IQCSamplePlanQueryDTO();
        dto.setSsccNb(MesBarCodeUtil.getSSCC(mesBarCode));
        List<IQCSamplePlanVO> samplePlan = getSamplePlan(dto);
        if (CollectionUtils.isEmpty(samplePlan)) {
            throw new ServiceException("没有该sscc" + MesBarCodeUtil.getSSCC(mesBarCode) + "对应的IQC抽样任务");
        }
        return samplePlan.get(0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirm(IQCSamplePlanDTO dto) {

        LambdaQueryWrapper<IQCSamplePlan> iqcQueryWrapper = new LambdaQueryWrapper<>();
        iqcQueryWrapper.eq(IQCSamplePlan::getSsccNb, dto.getSsccNb());
        iqcQueryWrapper.eq(IQCSamplePlan::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        iqcQueryWrapper.ne(IQCSamplePlan::getStatus,IQCStatusEnum.CANCEL.code());
        iqcQueryWrapper.ne(IQCSamplePlan::getStatus,IQCStatusEnum.FINISH.code());

        IQCSamplePlan iqcSamplePlan = samplePlanMapper.selectOne(iqcQueryWrapper);
        if (iqcSamplePlan == null) {
            throw new ServiceException("没有该sscc:" + dto.getSsccNb() + "对应的IQC抽样任务");
        }
        if (!iqcSamplePlan.getStatus().equals(IQCStatusEnum.WAITING_SAMPLE.code())) {
            throw new ServiceException("sscc:" + dto.getSsccNb() + "对应任务状态为:" + IQCStatusEnum.getDesc(iqcSamplePlan.getStatus()) + ",不可抽样");
        }
        iqcSamplePlan.setSampleQuantity(dto.getSampleQuantity());
        iqcSamplePlan.setSampleTime(new Date());
        iqcSamplePlan.setSampleUser(SecurityUtils.getUsername());
        if (iqcSamplePlan.getSampleQuantity() != iqcSamplePlan.getQuantity()) {
            iqcSamplePlan.setStatus(IQCStatusEnum.WAITING_BIN_IN.code());
        } else {
            iqcSamplePlan.setStatus(IQCStatusEnum.FINISH.code());
        }
        samplePlanMapper.updateById(iqcSamplePlan);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addShift(IQCWareShiftDTO dto) {
        LambdaQueryWrapper<IQCSamplePlan> iqcQueryWrapper = new LambdaQueryWrapper<>();
        iqcQueryWrapper.eq(IQCSamplePlan::getSsccNb, dto.getSscc());
        iqcQueryWrapper.eq(IQCSamplePlan::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        iqcQueryWrapper.eq(IQCSamplePlan::getStatus, IQCStatusEnum.WAITING_BIN_DOWN.code());
        IQCSamplePlan samplePlan = samplePlanMapper.selectOne(iqcQueryWrapper);
        if (Objects.isNull(samplePlan)) {
            throw new ServiceException("存在非IQC抽样任务数据或非待下架状态数据，请刷新后重新选择");
        }
        //查询库存
        LambdaQueryWrapper<Stock> stockQueryWrapper = new LambdaQueryWrapper<>();
        stockQueryWrapper.eq(Stock::getSsccNumber, dto.getSscc());
        stockQueryWrapper.eq(Stock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        Stock stock = stockService.getOne(stockQueryWrapper);
        if (AreaListConstants.mainAreaList.contains(stock.getAreaCode())) {
            throw new ServiceException("IQC移库只能选择外库数据");
        }
        WareShift wareShift = WareShift.builder().sourcePlantNb(stock.getPlantNb()).sourceWareCode(stock.getWareCode()).sourceAreaCode(stock.getAreaCode())
                .sourceBinCode(stock.getBinCode()).materialNb(stock.getMaterialNb()).batchNb(stock.getBatchNb()).expireDate(stock.getExpireDate())
                .ssccNb(stock.getSsccNumber()).deleteFlag(DeleteFlagStatus.FALSE.getCode()).moveType(MoveTypeEnums.WARE_SHIFT.getCode())
                .status(KanbanStatusEnum.WAITING_BIN_DOWN.value())
                .targetWareCode(dto.getTargetWareCode())
                .quantity(stock.getTotalStock())
                .build();
        wareShiftService.save(wareShift);

        samplePlan.setStatus(IQCStatusEnum.WARE_SHIFTING.code());
        samplePlanMapper.updateById(samplePlan);


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelWareShift(String ssccNb) {
        LambdaQueryWrapper<WareShift> wareShiftQueryWrapper = new LambdaQueryWrapper<>();
        wareShiftQueryWrapper.eq(WareShift::getSsccNb, ssccNb);
        wareShiftQueryWrapper.eq(WareShift::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        wareShiftQueryWrapper.ne(WareShift::getStatus, KanbanStatusEnum.FINISH.value());
        wareShiftQueryWrapper.ne(WareShift::getStatus, KanbanStatusEnum.CANCEL.value());
        wareShiftQueryWrapper.last("limit 1");
        WareShift wareShift = wareShiftService.getOne(wareShiftQueryWrapper);
        if (!wareShift.getStatus().equals(KanbanStatusEnum.WAITING_BIN_DOWN.value())) {
            throw new ServiceException("当前移库状态为: " + KanbanStatusEnum.getDesc(String.valueOf(wareShift.getStatus())) + ",不可以变更取样地点");
        }
        wareShift.setStatus(KanbanStatusEnum.CANCEL.value());
        wareShiftService.updateById(wareShift);

        //修改抽样任务状态为待下架
        LambdaQueryWrapper<IQCSamplePlan> iqcQueryWrapper = new LambdaQueryWrapper<>();
        iqcQueryWrapper.eq(IQCSamplePlan::getSsccNb, ssccNb);
        iqcQueryWrapper.eq(IQCSamplePlan::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        iqcQueryWrapper.eq(IQCSamplePlan::getStatus, IQCStatusEnum.WARE_SHIFTING.code());
        iqcQueryWrapper.last("limit 1");
        IQCSamplePlan samplePlan = this.getOne(iqcQueryWrapper);
        samplePlan.setStatus(IQCStatusEnum.WAITING_BIN_DOWN.code());
        this.updateById(samplePlan);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyQuantity(String ssccNb, Double quantity) {
        LambdaQueryWrapper<IQCSamplePlan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(IQCSamplePlan::getSsccNb, ssccNb);
        queryWrapper.eq(IQCSamplePlan::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        queryWrapper.ne(IQCSamplePlan::getStatus,IQCStatusEnum.CANCEL.code());
        queryWrapper.ne(IQCSamplePlan::getStatus,IQCStatusEnum.FINISH.code());

        IQCSamplePlan samplePlan = this.getOne(queryWrapper);
        if (samplePlan == null) {
            throw new ServiceException("该sscc" + ssccNb + "没有对应的抽样任务");
        }
        if (!(samplePlan.getStatus().equals(IQCStatusEnum.WAAITTING_ISSUE.code())
                || samplePlan.getStatus().equals(IQCStatusEnum.WAITING_BIN_DOWN.code()))) {
            throw new ServiceException("sscc:" + ssccNb + "对应任务状态为:" + IQCStatusEnum.getDesc(samplePlan.getStatus()) + ",不可修改");
        }
        samplePlan.setRecommendSampleQuantity(quantity);
        this.updateById(samplePlan);
    }

    @Override
    public void issueJob(Long[] ids) {
        LambdaQueryWrapper<IQCSamplePlan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(IQCSamplePlan::getId, ids);
        queryWrapper.eq(IQCSamplePlan::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        queryWrapper.ne(IQCSamplePlan::getStatus,IQCStatusEnum.CANCEL.code());
        queryWrapper.ne(IQCSamplePlan::getStatus,IQCStatusEnum.FINISH.code());

        List<IQCSamplePlan> samplePlanList = this.list(queryWrapper);
        if (!CollectionUtils.isEmpty(samplePlanList)) {
            ArrayList<WareShift> wareShiftList = new ArrayList<>();
            samplePlanList.forEach(item -> {
                if (item.getStatus() != IQCStatusEnum.WAAITTING_ISSUE.code()) {
                    throw new ServiceException("存在已下发或者已取消任务，请重新选择");
                }
                item.setStatus(IQCStatusEnum.WAITING_BIN_DOWN.code());
                if (!AreaListConstants.mainAreaList.contains(item.getAreaCode())) {
                    Stock stock = stockService.getAvailablesStockBySscc(item.getSsccNb());
                    WareShift wareShift = WareShift.builder().sourcePlantNb(stock.getPlantNb()).sourceWareCode(stock.getWareCode()).sourceAreaCode(stock.getAreaCode())
                            .sourceBinCode(stock.getBinCode()).materialNb(stock.getMaterialNb()).batchNb(stock.getBatchNb()).expireDate(stock.getExpireDate())
                            .ssccNb(stock.getSsccNumber()).deleteFlag(DeleteFlagStatus.FALSE.getCode()).moveType(MoveTypeEnums.WARE_SHIFT.getCode())
                            .status(KanbanStatusEnum.WAITING_BIN_DOWN.value())
                            .quantity(stock.getTotalStock())
                            .build();
                    wareShiftList.add(wareShift);

                    item.setStatus(IQCStatusEnum.WARE_SHIFTING.code());


                }
            });
            if (!CollectionUtils.isEmpty(wareShiftList)) {
                wareShiftService.saveBatch(wareShiftList);
            }
            this.updateBatchById(samplePlanList);
        }
    }


}
