package com.bosch.binin.service.impl;

import com.alibaba.druid.sql.ast.expr.SQLCaseExpr;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.binin.api.StockLog;
import com.bosch.binin.api.domain.*;
import com.bosch.binin.api.domain.dto.*;
import com.bosch.binin.api.domain.vo.BinAllocationVO;
import com.bosch.binin.api.domain.vo.StockVO;
import com.bosch.binin.api.enumeration.BinInStatusEnum;
import com.bosch.binin.api.enumeration.IQCStatusEnum;
import com.bosch.binin.api.enumeration.KanbanStatusEnum;
import com.bosch.binin.mapper.*;
import com.bosch.binin.service.*;
import com.bosch.masterdata.api.RemoteIQCService;
import com.bosch.masterdata.api.RemoteMasterDataService;
import com.bosch.masterdata.api.RemoteMaterialService;
import com.bosch.masterdata.api.domain.Ecn;
import com.bosch.masterdata.api.domain.Fsmp;
import com.bosch.masterdata.api.domain.Nmd;
import com.bosch.masterdata.api.domain.vo.AreaVO;
import com.bosch.masterdata.api.domain.vo.BinVO;
import com.bosch.masterdata.api.domain.vo.MaterialBinVO;
import com.bosch.masterdata.api.domain.vo.MaterialVO;
import com.bosch.masterdata.api.enumeration.*;
import com.bosch.storagein.api.RemoteMaterialInService;
import com.bosch.binin.api.domain.vo.BinInVO;
import com.bosch.masterdata.api.domain.Pallet;
import com.bosch.masterdata.api.RemotePalletService;
import com.bosch.storagein.api.domain.vo.MaterialInVO;
import com.bosch.storagein.api.domain.vo.MaterialReceiveVO;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.enums.MoveTypeEnums;
import com.ruoyi.common.core.enums.QualityStatusEnums;
import com.ruoyi.common.core.utils.DoubleMathUtil;
import com.ruoyi.common.core.utils.MesBarCodeUtil;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.utils.bean.BeanConverUtil;
import com.ruoyi.common.security.utils.SecurityUtils;
import lombok.Synchronized;
import org.aspectj.weaver.patterns.ThisOrTargetAnnotationPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.awt.image.SampleModel;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: UWH4SZH
 * @since: 10/19/2022 10:15
 * @description:
 */
@Service
public class BinInServiceImpl extends ServiceImpl<BinInMapper, BinIn> implements IBinInService {


    @Autowired
    private BinInMapper binInMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private StockLogMapper stockLogMapper;

    @Autowired
    private RemoteMaterialService remoteMaterialService;

    @Autowired
    private RemotePalletService remotePalletService;

    @Autowired
    private RemoteMaterialInService remoteMaterialInService;

    @Autowired
    private RemoteMasterDataService remoteMasterDataService;

    @Autowired
    private IBinAssignmentService binAssignmentService;

    @Autowired
    private MaterialKanbanMapper kanbanMapper;

    @Autowired
    private WareShiftMapper wareShiftMapper;

    @Lazy
    @Autowired
    private IWareShiftService wareShiftService;

    @Autowired
    private IStockService stockService;

    @Autowired
    private ManualTransferOrderMapper manualTransferOrderMapper;

    @Autowired
    private IQCSamplePlanMapper samplePlanMapper;

    @Autowired
    @Lazy
    private IIQCSamplePlanService samplePlanService;

    @Autowired
    private IIQCRuleService ruleService;

    @Autowired
    private RemoteIQCService remoteIQCService;


    @Override
    public List<BinInVO> selectBinVOList(BinInQueryDTO queryDTO) {
        return binInMapper.selectBinVOList(queryDTO);
    }

    @Override
    public String virtualPalletCode(String palletType) {
        R<Pallet> palletResult = remotePalletService.getByType(palletType);
        if (StringUtils.isNull(palletResult) || StringUtils.isNull(palletResult.getData())) {
            throw new ServiceException("托盘类型：" + palletType + " 不存在");
        }

        if (R.FAIL == palletResult.getCode()) {
            throw new ServiceException(palletResult.getMsg());
        }

        Pallet pallet = palletResult.getData();
        String virtualPrefixCode = pallet.getVirtualPrefixCode();

        return "V-" + virtualPrefixCode + "-" + System.currentTimeMillis();
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    @Synchronized
    public BinInVO getByMesBarCode(String mesBarCode) {
        String sscc = MesBarCodeUtil.getSSCC(mesBarCode);

        BinInVO binInVO = binInMapper.selectBySsccNumber(sscc);
        if (binInVO != null) {
            return binInVO;
        }
        MaterialInVO materialInVO = getMaterialInVO(mesBarCode);

        String materialNb = MesBarCodeUtil.getMaterialNb(mesBarCode);
        String batchNb = MesBarCodeUtil.getBatchNb(mesBarCode);
        MaterialVO materialVO = getMaterialVOByCode(MesBarCodeUtil.getMaterialNb(mesBarCode));

        if (binInVO == null) {
            R<List<MaterialBinVO>> materialBinVOResullt = remoteMasterDataService.getListByMaterial(materialNb);
            if (StringUtils.isNull(materialBinVOResullt) || CollectionUtils.isEmpty(materialBinVOResullt.getData())) {
                throw new ServiceException("该物料：" + materialNb + " 分配规则有误");
            }

            if (R.FAIL == materialBinVOResullt.getCode()) {
                throw new ServiceException(materialBinVOResullt.getMsg());
            }

            BinAllocationDTO allocationDTO = new BinAllocationDTO();
            allocationDTO.setMesBarCode(mesBarCode);
            //获取托盘
            allocationDTO.setPalletType(materialVO.getPalletType());
            allocationDTO.setWareCode(SecurityUtils.getWareCode());

            //获取托盘详情
            R<Pallet> palletR = remotePalletService.getByType(materialVO.getPalletType());
            if (!palletR.isSuccess()) {
                throw new ServiceException("获取托盘详情失败");
            }
            Pallet pallet = palletR.getData();
            if (pallet == null) {
                throw new ServiceException("未获取到托盘数据");
            }


            BinAllocationVO binAllocationVO = binAssignmentService.getBinAllocationVO(allocationDTO);

            BinVO binVO = getBinVOByBinCode(binAllocationVO.getRecommendBinCode());


            BinIn binIn = new BinIn();
            binIn.setSsccNumber(sscc);
            binIn.setQuantity(materialInVO.getQuantity());
            binIn.setMaterialNb(materialNb);
            binIn.setBatchNb(batchNb);
            binIn.setExpireDate(MesBarCodeUtil.getExpireDate(mesBarCode));
            binIn.setPalletType(materialVO.getPalletType());
            //设置托盘编码,虚拟托盘直接分配编码
            binIn.setPalletCode(pallet.getIsVirtual().equals(1) ? "V-" + pallet.getVirtualPrefixCode() + "-" + System.currentTimeMillis() : null);
            binIn.setRecommendBinCode(binAllocationVO.getRecommendBinCode());
            binIn.setStatus(BinInStatusEnum.PROCESSING.value());
            binIn.setRecommendFrameId(binVO.getFrameId());
            binIn.setRecommendFrameCode(binVO.getFrameCode());
            binIn.setWareCode(SecurityUtils.getWareCode());

            binIn.setMoveType(MoveTypeEnums.BININ.getCode());
            binIn.setFromPurchaseOrder(materialInVO.getFromPurchaseOrder());
            binIn.setPlantNb(materialInVO.getPlantNb());
            binInMapper.insert(binIn);
        }


        return binInMapper.selectBySsccNumber(sscc);
    }

    @Override
    public BinInVO allocateToBin(String mesBarCode) {
        String sscc = MesBarCodeUtil.getSSCC(mesBarCode);

        BinInVO binInVO = binInMapper.selectBySsccNumber(sscc);
        if (binInVO != null) {
            return binInVO;
        }

        String materialNb = MesBarCodeUtil.getMaterialNb(mesBarCode);
        String batchNb = MesBarCodeUtil.getBatchNb(mesBarCode);
        MaterialVO materialVO = getMaterialVOByCode(MesBarCodeUtil.getMaterialNb(mesBarCode));

        if (binInVO == null) {
            R<List<MaterialBinVO>> materialBinVOResullt = remoteMasterDataService.getListByMaterial(materialNb);
            if (StringUtils.isNull(materialBinVOResullt) || CollectionUtils.isEmpty(materialBinVOResullt.getData())) {
                throw new ServiceException("该物料：" + materialNb + " 分配规则有误");
            }

            if (R.FAIL == materialBinVOResullt.getCode()) {
                throw new ServiceException(materialBinVOResullt.getMsg());
            }

            BinAllocationDTO allocationDTO = new BinAllocationDTO();
            allocationDTO.setMesBarCode(mesBarCode);
            //获取托盘
            allocationDTO.setPalletType(materialVO.getPalletType());
            allocationDTO.setWareCode(SecurityUtils.getWareCode());

            //获取托盘详情
            R<Pallet> palletR = remotePalletService.getByType(materialVO.getPalletType());
            if (!palletR.isSuccess()) {
                throw new ServiceException("获取托盘详情失败");
            }
            Pallet pallet = palletR.getData();
            if (pallet == null) {
                throw new ServiceException("未获取到托盘数据");
            }


            BinAllocationVO binAllocationVO = binAssignmentService.getBinAllocationVO(allocationDTO);

            BinVO binVO = getBinVOByBinCode(binAllocationVO.getRecommendBinCode());


            BinIn binIn = new BinIn();
            binIn.setSsccNumber(sscc);
            binIn.setQuantity(Double.valueOf(MesBarCodeUtil.getQuantity(mesBarCode)));
            binIn.setMaterialNb(materialNb);
            binIn.setBatchNb(batchNb);
            binIn.setExpireDate(MesBarCodeUtil.getExpireDate(mesBarCode));
            binIn.setPalletType(materialVO.getPalletType());
            //设置托盘编码,虚拟托盘直接分配编码
            binIn.setPalletCode(pallet.getIsVirtual().equals(1) ? "V-" + pallet.getVirtualPrefixCode() + "-" + System.currentTimeMillis() : null);
            binIn.setRecommendBinCode(binAllocationVO.getRecommendBinCode());
            binIn.setStatus(BinInStatusEnum.PROCESSING.value());
            binIn.setRecommendFrameId(binVO.getFrameId());
            binIn.setRecommendFrameCode(binVO.getFrameCode());
            binIn.setWareCode(SecurityUtils.getWareCode());

            binIn.setMoveType(MoveTypeEnums.BININ.getCode());
//            binIn.setFromPurchaseOrder(materialInVO.getFromPurchaseOrder());
            binIn.setPlantNb(binVO.getPlantNb());
            binInMapper.insert(binIn);
        }


        return binInMapper.selectBySsccNumber(sscc);
    }

    public MaterialVO getMaterialVOByCode(String materialNb) {
        R<MaterialVO> materialVORes = remoteMaterialService.getInfoByMaterialCode(materialNb);
        if (StringUtils.isNull(materialVORes) || Objects.isNull(materialVORes.getData())) {
            throw new ServiceException("该物料：" + materialNb + " 不存在");
        }

        if (R.FAIL == materialVORes.getCode()) {
            throw new ServiceException(materialVORes.getMsg());
        }
        return materialVORes.getData();

    }

    @Override
    public List<BinInVO> currentUserData(BinInQueryDTO queryDTO) {
        return binInMapper.currentUserData(queryDTO);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public BinInVO performBinIn(BinInDTO binInDTO) {
        String mesBarCode = binInDTO.getMesBarCode();
        String sscc = MesBarCodeUtil.getSSCC(mesBarCode);
        String materialNb = MesBarCodeUtil.getMaterialNb(mesBarCode);

        LambdaQueryWrapper<BinIn> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BinIn::getSsccNumber, sscc).eq(BinIn::getDeleteFlag, 0);
        BinIn binIn = binInMapper.selectOne(lambdaQueryWrapper);


        if (binIn == null) {
            throw new ServiceException("物料号" + materialNb + "暂无上架任务");
        }

        if (BinInStatusEnum.FINISH.value() == binIn.getStatus()) {
            throw new ServiceException("物料号" + materialNb + "已经上架");
        }

        if (StringUtils.isEmpty(binIn.getPalletCode()) && StringUtils.isEmpty(binInDTO.getPalletCode())) {
            throw new ServiceException("实物托盘码不能为空");
        }
        BinVO actualBinVO = getBinVOByBinCode(binInDTO.getActualBinCode());

        if (!binInDTO.getActualBinCode().equals(binIn.getRecommendBinCode())) {
            //不在的时候，看actual bin code在不在分配规则内
            R<List<MaterialBinVO>> materialBinVOResullt = remoteMasterDataService.getListByMaterial(materialNb);
            if (StringUtils.isNull(materialBinVOResullt) || StringUtils.isNull(materialBinVOResullt.getData())) {
                throw new ServiceException("该物料：" + materialNb + " 暂无分配规则");
            }

            if (R.FAIL == materialBinVOResullt.getCode()) {
                throw new ServiceException(materialBinVOResullt.getMsg());
            }

            List<MaterialBinVO> materialBinVOS = materialBinVOResullt.getData();
//            List<String> frameCodeList = materialBinVOS.stream().map(MaterialBinVO::getFrameTypeCode).collect(Collectors.toList());
//            if (StringUtils.isEmpty(frameCodeList) || !frameCodeList.contains(actualBinVO.getFrameTypeCode())) {
//                throw new ServiceException("该物料" + materialNb + " 不能分配到" + binInDTO.getActualBinCode());
//            }
            LambdaQueryWrapper<BinIn> finishQueryWrapper = new LambdaQueryWrapper<>();
            finishQueryWrapper.eq(BinIn::getActualBinCode, binInDTO.getActualBinCode()).eq(BinIn::getStatus, BinInStatusEnum.FINISH.value()).eq(BinIn::getDeleteFlag, 0);
            List<BinIn> binInFinish = binInMapper.selectList(finishQueryWrapper);
            if (!CollectionUtils.isEmpty(binInFinish)) {
                throw new ServiceException("该库位" + binInDTO.getActualBinCode() + " 已经被占用");
            }

            LambdaQueryWrapper<BinIn> processingQueryWrapper = new LambdaQueryWrapper<>();
            processingQueryWrapper.eq(BinIn::getRecommendBinCode, binInDTO.getActualBinCode()).eq(BinIn::getStatus, BinInStatusEnum.PROCESSING.value()).eq(BinIn::getDeleteFlag, 0);
            List<BinIn> binInProcessing = binInMapper.selectList(processingQueryWrapper);
            if (!CollectionUtils.isEmpty(binInProcessing)) {
                throw new ServiceException("该库位" + binInDTO.getActualBinCode() + " 已经被分配其他托");
            }


        }
        binIn.setActualBinCode(binInDTO.getActualBinCode());
        binIn.setActualFrameCode(actualBinVO.getFrameCode());
        binIn.setActualFrameId(actualBinVO.getFrameId());
        binIn.setUpdateBy(SecurityUtils.getUsername());
        binIn.setStatus(BinInStatusEnum.FINISH.value());
        binIn.setUpdateTime(new Date());
        binIn.setAreaCode(actualBinVO.getAreaCode());
        if (StringUtils.isEmpty(binIn.getPalletCode())) {
            binIn.setPalletCode(binInDTO.getPalletCode());
        }
        saveOrUpdate(binIn);

        //插入库存
        Stock stock = new Stock();
        stock.setPlantNb(binIn.getPlantNb());
        stock.setWareCode(actualBinVO.getWareCode());
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
        stock.setQualityStatus(QualityStatusEnums.WAITING_QUALITY.getCode());
        stock.setFromPurchaseOrder(binIn.getFromPurchaseOrder());
        stock.setAreaCode(binIn.getAreaCode());
        stock.setPalletCode(binIn.getPalletCode());
        stockMapper.insert(stock);

        //处理库存日志表
        StockLog stockLog = BeanConverUtil.conver(stock, StockLog.class);
        stockLog.setMoveType(MoveTypeEnums.BININ.getCode());
        stockLogMapper.insert(stockLog);
        return binInMapper.selectBySsccNumber(binIn.getSsccNumber());
    }

    @Override
    public BinInVO performBinInWithIQC(BinInDTO binInDTO) {
        String mesBarCode = binInDTO.getMesBarCode();
        String sscc = MesBarCodeUtil.getSSCC(mesBarCode);
        String materialNb = MesBarCodeUtil.getMaterialNb(mesBarCode);

        LambdaQueryWrapper<BinIn> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BinIn::getSsccNumber, sscc).eq(BinIn::getDeleteFlag, 0);
        BinIn binIn = binInMapper.selectOne(lambdaQueryWrapper);


        if (binIn == null) {
            throw new ServiceException("物料号" + materialNb + "暂无上架任务");
        }

        if (BinInStatusEnum.FINISH.value() == binIn.getStatus()) {
            throw new ServiceException("物料号" + materialNb + "已经上架");
        }

        if (StringUtils.isEmpty(binIn.getPalletCode()) && StringUtils.isEmpty(binInDTO.getPalletCode())) {
            throw new ServiceException("实物托盘码不能为空");
        }
        BinVO actualBinVO = getBinVOByBinCode(binInDTO.getActualBinCode());

        //2023.3.4 客户修改。实际上架可以上架到任意一个库位，不做限制
        if (!binInDTO.getActualBinCode().equals(binIn.getRecommendBinCode())) {
//            //不在的时候，看actual bin code在不在分配规则内
//            R<List<MaterialBinVO>> materialBinVOResullt = remoteMasterDataService.getListByMaterial(materialNb);
//            if (StringUtils.isNull(materialBinVOResullt) || StringUtils.isNull(materialBinVOResullt.getData())) {
//                throw new ServiceException("该物料：" + materialNb + " 暂无分配规则");
//            }
//
//            if (R.FAIL == materialBinVOResullt.getCode()) {
//                throw new ServiceException(materialBinVOResullt.getMsg());
//            }
//
//            List<MaterialBinVO> materialBinVOS = materialBinVOResullt.getData();
//
//
//            List<String> frameCodeList = materialBinVOS.stream().map(MaterialBinVO::getFrameTypeCode).collect(Collectors.toList());
//            if (StringUtils.isEmpty(frameCodeList) || !frameCodeList.contains(actualBinVO.getFrameTypeCode())) {
//                throw new ServiceException("该物料" + materialNb + " 不能分配到" + binInDTO.getActualBinCode());
//            }

            LambdaQueryWrapper<BinIn> finishQueryWrapper = new LambdaQueryWrapper<>();
            finishQueryWrapper.eq(BinIn::getActualBinCode, binInDTO.getActualBinCode()).eq(BinIn::getStatus, BinInStatusEnum.FINISH.value()).eq(BinIn::getDeleteFlag, 0);
            List<BinIn> binInFinish = binInMapper.selectList(finishQueryWrapper);
            if (!CollectionUtils.isEmpty(binInFinish)) {
                throw new ServiceException("该库位" + binInDTO.getActualBinCode() + " 已经被占用");
            }

            LambdaQueryWrapper<BinIn> processingQueryWrapper = new LambdaQueryWrapper<>();
            processingQueryWrapper.eq(BinIn::getRecommendBinCode, binInDTO.getActualBinCode()).eq(BinIn::getStatus, BinInStatusEnum.PROCESSING.value()).eq(BinIn::getDeleteFlag, 0);
            List<BinIn> binInProcessing = binInMapper.selectList(processingQueryWrapper);
            if (!CollectionUtils.isEmpty(binInProcessing)) {
                throw new ServiceException("该库位" + binInDTO.getActualBinCode() + " 已经被分配其他托");
            }


        }
        binIn.setActualBinCode(binInDTO.getActualBinCode());
        binIn.setActualFrameCode(actualBinVO.getFrameCode());
        binIn.setActualFrameId(actualBinVO.getFrameId());
        binIn.setUpdateBy(SecurityUtils.getUsername());
        binIn.setStatus(BinInStatusEnum.FINISH.value());
        binIn.setUpdateTime(new Date());
        binIn.setAreaCode(actualBinVO.getAreaCode());
        if (StringUtils.isEmpty(binIn.getPalletCode())) {
            binIn.setPalletCode(binInDTO.getPalletCode());
        }
        saveOrUpdate(binIn);

        //插入库存
        Stock stock = new Stock();
        stock.setPlantNb(binIn.getPlantNb());
        stock.setWareCode(actualBinVO.getWareCode());
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
        stock.setQualityStatus(QualityStatusEnums.WAITING_QUALITY.getCode());
        stock.setFromPurchaseOrder(binIn.getFromPurchaseOrder());
        stock.setAreaCode(binIn.getAreaCode());
        stock.setPalletCode(binIn.getPalletCode());
        stockMapper.insert(stock);

        //处理库存日志表
        StockLog stockLog = BeanConverUtil.conver(stock, StockLog.class);
        stockLog.setMoveType(MoveTypeEnums.BININ.getCode());
        stockLogMapper.insert(stockLog);

//        // 更新kanban和移库,转储单状态
//        updateKanbanWareShiftStatus(stock, sscc);

        MaterialVO materialVO = getMaterialVOByCode(MesBarCodeUtil.getMaterialNb(mesBarCode));


        //质检
        if ("Y".equals(materialVO.getIqc())) {


            boolean lastFlag = true;

            //获取收货的同批次信息
            R<List<MaterialReceiveVO>> sameBatchListR = remoteMaterialInService.getSameBatchList(materialNb, binIn.getBatchNb());

            if (StringUtils.isNull(sameBatchListR) || StringUtils.isNull(sameBatchListR.getData())) {
                throw new ServiceException("不存在该批次信息");
            }


            if (R.FAIL == sameBatchListR.getCode()) {
                throw new ServiceException(sameBatchListR.getMsg());
            }
            List<MaterialReceiveVO> sameBatchList = sameBatchListR.getData();
            //获取上架的同批次信息
            LambdaQueryWrapper<BinIn> binInQueryWrapper = new LambdaQueryWrapper<>();
            binInQueryWrapper.eq(BinIn::getMaterialNb, materialNb);
            binInQueryWrapper.eq(BinIn::getBatchNb, binIn.getBatchNb());
            binInQueryWrapper.eq(BinIn::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
            List<BinIn> binInList = binInMapper.selectList(binInQueryWrapper);
            if (CollectionUtils.isEmpty(sameBatchList) || CollectionUtils.isEmpty(binInList) || binInList.size() != sameBatchList.size()) {
                lastFlag = false;
            }
            List<IQCSamplePlan> samplePlanList = new ArrayList<>();
            if (lastFlag && DeparementEnum.NMD.getCode().equals(materialVO.getCell())) {//NMD
                samplePlanList = dealNMDIQCProcess(materialVO, binIn.getBatchNb(), binInList, sameBatchList);
            } else if (lastFlag && DeparementEnum.ECN.getCode().equals(materialVO.getCell())) {//ECN
                samplePlanList = dealECNIQCProcess(materialVO, binIn.getBatchNb(), binInList, sameBatchList);
            } else if (lastFlag && DeparementEnum.FSMP.getCode().equals(materialVO.getCell())) {//FSMP
                samplePlanList = dealFSMPIQCProces(materialVO, binIn.getBatchNb(), binInList, sameBatchList);
            }
            //下发的时候去新增外库任务。
            // 如果是外库的，新增移库任务
//            if (!CollectionUtils.isEmpty(samplePlanList)) {
//                if ("7752".equals(samplePlanList.get(0).getPlantNb())) {
//
//                    List<String> samplanSsccList = samplePlanList.stream().map(IQCSamplePlan::getSsccNb).collect(Collectors.toList());
//                    List<BinIn> inList = binInList.stream().filter(item -> samplanSsccList.contains(item.getSsccNumber())).collect(Collectors.toList());
//                    List<WareShift> wareShiftList = new ArrayList<>();
//                    inList.stream().forEach(item -> {
//                        WareShift wareShift = WareShift.builder().sourcePlantNb(item.getPlantNb()).sourceWareCode(item.getWareCode()).sourceAreaCode(item.getAreaCode())
//                                .sourceBinCode(item.getActualBinCode()).materialNb(item.getMaterialNb()).batchNb(item.getBatchNb()).expireDate(item.getExpireDate())
//                                .ssccNb(item.getSsccNumber()).deleteFlag(DeleteFlagStatus.FALSE.getCode()).moveType(MoveTypeEnums.WARE_SHIFT.getCode())
//                                .status(KanbanStatusEnum.WAITING_BIN_DOWN.value())
//                                .quantity(item.getQuantity())
//                                .build();
//                        wareShiftList.add(wareShift);
//                    });
//                    wareShiftService.saveBatch(wareShiftList);
//                }
//            }
        }


        return binInMapper.selectBySsccNumber(binIn.getSsccNumber());
    }

    private List<IQCSamplePlan> dealFSMPIQCProces(MaterialVO materialVO, String batchNb, List<BinIn> binInList, List<MaterialReceiveVO> sameBatchList) {
        R<Fsmp> fsmpR = remoteIQCService.getFsmpByMaterialNb(materialVO.getCode());
        if (StringUtils.isNull(fsmpR) || StringUtils.isNull(fsmpR.getData())) {
            throw new ServiceException("FSMP不存在该物料的IQC信息，请先维护主数据");
        }
        Fsmp fsmp = fsmpR.getData();
        List<IQCSamplePlan> samplePlanList = new ArrayList<>();

        if (fsmp.getClassification().equals(FsmpClassificationEnum.A.getDesc())&&sameBatchList.size() > 3) {//如果是A且大于三托，取非连续的三托
            List<String> ssccList = sameBatchList.stream().map(MaterialReceiveVO::getSsccNumber).collect(Collectors.toList());
            List<String> randomSscc = getRandomNonConsecutive(ssccList);
            List<BinIn> binIns = binInList.stream().filter(item -> randomSscc.contains(item.getSsccNumber())).collect(Collectors.toList());
            samplePlanList = convertToSamplePlans(binIns, materialVO, null);
        } else if (fsmp.getClassification().equals(FsmpClassificationEnum.B.getDesc())||sameBatchList.size() <= 3){//随机下架一托
            Collections.shuffle(binInList);
            IQCSamplePlan samplePlan = convertToSamplePlan(binInList.get(0), null, materialVO);
            samplePlanList.add(samplePlan);
        }
        samplePlanService.saveBatch(samplePlanList);

        //冻结库存
        List<String> samplePlanSsccList = samplePlanList.stream().map(IQCSamplePlan::getSsccNb).collect(Collectors.toList());
        LambdaQueryWrapper<Stock> stockLambdaQueryWrapper = new LambdaQueryWrapper<>();
        stockLambdaQueryWrapper.in(Stock::getSsccNumber, samplePlanSsccList);
        stockLambdaQueryWrapper.eq(Stock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        List<Stock> stockList = stockService.list(stockLambdaQueryWrapper);
        stockList.stream().forEach(item -> {
            item.setFreezeStock(item.getTotalStock());
            item.setAvailableStock(item.getTotalStock() - item.getFreezeStock());
        });
        stockService.updateBatchById(stockList);

        return samplePlanList;
    }

    /**
     * 不连续三托
     *
     * @param list
     * @return
     */
    private List<String> getRandomNonConsecutive(List<String> list) {
        if (list == null || list.size() < 3) {
            throw new IllegalArgumentException("List must contain at least three elements.");
        }

        long[] arr = list.stream().mapToLong(Long::parseLong).sorted().toArray();

        int count = 0;
        long[] result = new long[3];
        int index = new Random().nextInt(arr.length);
        long current = arr[index];
        result[count] = current;
        count++;

        while (count < 3) {
            long next = current + 2;
            int i = Arrays.binarySearch(arr, next);
            if (i < 0) {
                i = -i - 1;
            }
            if (i >= arr.length - count) {
                break;
            }
            long temp = arr[i + new Random().nextInt(arr.length - i - count)];
            if (Math.abs(temp - current) > 1) {
                result[count] = temp;
                current = temp;
                count++;
            }
        }

        List<String> resList = new ArrayList<>();
        for (long num : result) {
            resList.add(String.valueOf(num));
        }
        return resList;
    }

    private List<IQCSamplePlan> dealECNIQCProcess(MaterialVO materialVO, String batchNb, List<BinIn> binInList, List<MaterialReceiveVO> sameBatchList) {

        R<Ecn> ecnR = remoteIQCService.getEcnByMaterialNb(materialVO.getCode());
        if (StringUtils.isNull(ecnR) || StringUtils.isNull(ecnR.getData())) {
            throw new ServiceException("ECN不存在该物料的IQC信息，请先维护主数据");
        }
        Ecn ecn = ecnR.getData();
        List<IQCSamplePlan> samplePlanList = new ArrayList<>();

        if (EcnClassificationEnum.TTS.getDesc().equals(ecn.getClassification())) {//TTS
            samplePlanList = dealEcnTtsIqc(materialVO, ecn, binInList, sameBatchList);
        } else if (EcnClassificationEnum.UNTTS.getDesc().equals(ecn.getClassification())//非TTS
                || EcnClassificationEnum.HGG.getDesc().equals(ecn.getClassification())//皇冠盖
                || EcnClassificationEnum.SMS.getDesc().equals(ecn.getClassification())) {//说明书&标签
            samplePlanList = dealSameBatchQuantity(materialVO, ecn, binInList, sameBatchList);
            //修改单位为"件中取量"
            samplePlanList.forEach(item->item.setUnit("件中取量"));
        } else if (EcnClassificationEnum.ZX.getDesc().equals(ecn.getClassification())) {//国内产品纸箱
            Collections.shuffle(binInList);
            BinIn binIn = binInList.get(0);
            IQCSamplePlan samplePlan = convertToSamplePlan(binIn, Double.valueOf(2), materialVO);
            samplePlanList.add(samplePlan);
        } else if (EcnClassificationEnum.BLP.getDesc().equals(ecn.getClassification())) {//玻璃瓶
            samplePlanList = dealEcnBlpProcess(materialVO, binInList, sameBatchList);
        }

        samplePlanService.saveBatch(samplePlanList);

        //冻结库存
        List<String> samplePlanSsccList = samplePlanList.stream().map(IQCSamplePlan::getSsccNb).collect(Collectors.toList());
        LambdaQueryWrapper<Stock> stockLambdaQueryWrapper = new LambdaQueryWrapper<>();
        stockLambdaQueryWrapper.in(Stock::getSsccNumber, samplePlanSsccList);
        stockLambdaQueryWrapper.eq(Stock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        List<Stock> stockList = stockService.list(stockLambdaQueryWrapper);
        stockList.stream().forEach(item -> {
            item.setFreezeStock(item.getTotalStock());
            item.setAvailableStock(item.getTotalStock() - item.getFreezeStock());
        });
        stockService.updateBatchById(stockList);

        return samplePlanList;


    }

    private List<IQCSamplePlan> dealEcnBlpProcess(MaterialVO materialVO, List<BinIn> binInList, List<MaterialReceiveVO> sameBatchList) {
        double quantity = sameBatchList.stream().mapToDouble(MaterialReceiveVO::getQuantity).sum();
        NMDIQCRuleDTO ruleDTO = new NMDIQCRuleDTO();
        ruleDTO.setQuantity(quantity);
        ruleDTO.setCheckLevel("Ⅰ");
        NMDIQCRule iqcRule = ruleService.getNMDIQCRule(ruleDTO);
        //抽样量
        Integer sampleQuantity = iqcRule.getNormal();
        //总托数
        int totalPallet = sameBatchList.size();
        List<IQCSamplePlan> res = new ArrayList<>();

        if (totalPallet <= 3) {
            double sample = Math.ceil(sampleQuantity / totalPallet);
            res = convertToSamplePlans(binInList, materialVO, sample);
        } else if (totalPallet > 3 && totalPallet <= 300) {
            Collections.shuffle(binInList);
            List<BinIn> binIns = binInList.subList(0, (int) Math.round(Math.sqrt(totalPallet) + 1));
            double sample = Math.ceil(sampleQuantity / (Math.sqrt(totalPallet) + 1));
            res = convertToSamplePlans(binIns, materialVO, sample);
        } else {
            Collections.shuffle(binInList);
            List<BinIn> binIns = binInList.subList(0, (int) Math.round(Math.sqrt(totalPallet) / 2 + 1));
            double sample = Math.ceil(sampleQuantity / (Math.sqrt(totalPallet) / 2 + 1));
            res = convertToSamplePlans(binIns, materialVO, sample);
        }

        return res;
    }

    private List<IQCSamplePlan> dealEcnTtsIqc(MaterialVO materialVO, Ecn ecn, List<BinIn> binInList, List<MaterialReceiveVO> sameBatchList) {
        //总托数
        int totalPallet = sameBatchList.size();
        List<IQCSamplePlan> samplePlanList = new ArrayList<>();

        if (EcnPlanEnum.A.getDesc().equals(ecn.getPlan())) {
            double ceil = Math.ceil(Math.sqrt(totalPallet));
            Collections.shuffle(binInList);
            List<BinIn> sampleBinInList = binInList.subList(0, (int) ceil);
            samplePlanList = convertToSamplePlans(sampleBinInList, materialVO, Double.valueOf(0));
        } else if (EcnPlanEnum.C.getDesc().equals(ecn.getPlan())) {
            samplePlanList = convertToSamplePlans(binInList, materialVO, Double.valueOf(0));
        } else if (EcnPlanEnum.B.getDesc().equals(ecn.getPlan())) {
            samplePlanList = dealSameBatchQuantity(materialVO, ecn, binInList, sameBatchList);
            //单位修改为件中取量
            samplePlanList.forEach(item->item.setUnit("件中取量"));

        }
        return samplePlanList;


    }

    private List<IQCSamplePlan> dealSameBatchQuantity(MaterialVO materialVO, Ecn ecn, List<BinIn> binInList, List<MaterialReceiveVO> sameBatchList) {
        double quantity = sameBatchList.stream().mapToDouble(MaterialReceiveVO::getQuantity).sum();
        double criteria = Math.ceil(quantity / materialVO.getPackageWeight().doubleValue());
        if (criteria <= 3) {
            return convertToSamplePlans(binInList, materialVO, Double.valueOf(0));
        } else if (criteria > 3 && criteria <= 300) {
            int sampleQuantity = (int) Math.round(Math.sqrt(criteria) + 1);
            return generateByQuantity(binInList, sampleQuantity, materialVO);
        } else {
            int sampleQuantity = (int) Math.round(Math.sqrt(criteria) / 2 + 1);
            return generateByQuantity(binInList, sampleQuantity, materialVO);
        }
    }

    private List<IQCSamplePlan> generateByQuantity(List<BinIn> binInList, Integer quantity, MaterialVO materialVO) {
        Collections.shuffle(binInList);
        List<BinIn> list = new ArrayList<>();
        int count = 0;
        for (BinIn binIn : binInList) {
            list.add(binIn);
            count += binIn.getQuantity();
            if (count >= quantity) {
                break;
            }
        }
        double sum = list.stream().mapToDouble(BinIn::getQuantity).sum();
        double abs = Math.abs(quantity - sum);
        List<IQCSamplePlan> res = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            BinIn binIn = list.get(i);
            IQCSamplePlan iqcSamplePlan = new IQCSamplePlan();
            iqcSamplePlan.setSsccNb(binIn.getSsccNumber());
            iqcSamplePlan.setCell(materialVO.getCell());
            iqcSamplePlan.setMaterialNb(binIn.getMaterialNb());
            iqcSamplePlan.setBinDownCode(binIn.getActualBinCode());
            iqcSamplePlan.setBinDownTime(new Date());
            if (abs > 0) {
                if (i != list.size() - 1) {
                    iqcSamplePlan.setRecommendSampleQuantity(binIn.getQuantity());
                } else {
                    iqcSamplePlan.setRecommendSampleQuantity(binIn.getQuantity() - abs);
                }
            } else {
                iqcSamplePlan.setRecommendSampleQuantity(binIn.getQuantity());
            }
//            iqcSamplePlan.setStatus(binIn.getPlantNb().equals("7751") ? IQCStatusEnum.WAITING_BIN_DOWN.code() : IQCStatusEnum.WARE_SHIFTING.code());
            iqcSamplePlan.setStatus(IQCStatusEnum.WAITING_BIN_IN.code());
            iqcSamplePlan.setBatchNb(binIn.getBatchNb());
            iqcSamplePlan.setWareCode(binIn.getWareCode());
            iqcSamplePlan.setQuantity(binIn.getQuantity());
            iqcSamplePlan.setPlantNb(binIn.getPlantNb());


            iqcSamplePlan.setExpireDate(binIn.getExpireDate());
            iqcSamplePlan.setUnit(materialVO.getUnit());
            res.add(iqcSamplePlan);
        }
        return res;
    }

    private IQCSamplePlan convertToSamplePlan(BinIn binIn, Double quantity, MaterialVO materialVO) {
        IQCSamplePlan iqcSamplePlan = new IQCSamplePlan();
        iqcSamplePlan.setSsccNb(binIn.getSsccNumber());
        iqcSamplePlan.setCell(materialVO.getCell());
        iqcSamplePlan.setMaterialNb(binIn.getMaterialNb());
        iqcSamplePlan.setBinDownCode(binIn.getActualBinCode());
        iqcSamplePlan.setBinDownTime(new Date());
        iqcSamplePlan.setRecommendSampleQuantity(quantity);
//        iqcSamplePlan.setStatus(binIn.getPlantNb().equals("7751") ? IQCStatusEnum.WAITING_BIN_DOWN.code() : IQCStatusEnum.WARE_SHIFTING.code());
        iqcSamplePlan.setStatus(IQCStatusEnum.WAAITTING_ISSUE.code());
        iqcSamplePlan.setBatchNb(binIn.getBatchNb());
        iqcSamplePlan.setWareCode(binIn.getWareCode());
        iqcSamplePlan.setQuantity(binIn.getQuantity());
        iqcSamplePlan.setExpireDate(binIn.getExpireDate());
        iqcSamplePlan.setPlantNb(binIn.getPlantNb());
        iqcSamplePlan.setUnit(materialVO.getUnit());

        return iqcSamplePlan;
    }


    private List<IQCSamplePlan> convertToSamplePlans(List<BinIn> binInList, MaterialVO materialVO, Double sampleQuantity) {
        List<IQCSamplePlan> list = new ArrayList<>();
        binInList.stream().forEach(binIn -> {
            IQCSamplePlan iqcSamplePlan = new IQCSamplePlan();
            iqcSamplePlan.setSsccNb(binIn.getSsccNumber());
            iqcSamplePlan.setCell(materialVO.getCell());
            iqcSamplePlan.setMaterialNb(binIn.getMaterialNb());
            iqcSamplePlan.setBinDownCode(binIn.getActualBinCode());
            iqcSamplePlan.setBinDownTime(new Date());
            iqcSamplePlan.setRecommendSampleQuantity(sampleQuantity);
//            iqcSamplePlan.setStatus(binIn.getPlantNb().equals("7751") ? IQCStatusEnum.WAITING_BIN_DOWN.code() : IQCStatusEnum.WARE_SHIFTING.code());
            iqcSamplePlan.setStatus(IQCStatusEnum.WAAITTING_ISSUE.code());
            iqcSamplePlan.setBatchNb(binIn.getBatchNb());
            iqcSamplePlan.setWareCode(binIn.getWareCode());
            iqcSamplePlan.setQuantity(binIn.getQuantity());
            iqcSamplePlan.setExpireDate(binIn.getExpireDate());
            iqcSamplePlan.setPlantNb(binIn.getPlantNb());
            iqcSamplePlan.setUnit(materialVO.getUnit());
            list.add(iqcSamplePlan);

        });
        return list;
    }


    private List<IQCSamplePlan> dealNMDIQCProcess(MaterialVO materialVO, String batchNb, List<BinIn> binInList, List<MaterialReceiveVO> sameBatchList) {

        double quantity = sameBatchList.stream().mapToDouble(MaterialReceiveVO::getQuantity).sum();

        R<Nmd> iqcInfoR = remoteIQCService.getNmdByMaterialNb(materialVO.getCode());
        if (StringUtils.isNull(iqcInfoR) || StringUtils.isNull(iqcInfoR.getData())) {
            throw new ServiceException("不存在该物料的IQC信息");
        }
        Nmd nmd = iqcInfoR.getData();
        double sampleQuantity = 0;
        if (nmd.getClassification() == NmdClassificationEnum.A.getCode()) {
            NMDIQCRuleDTO ruleDTO = new NMDIQCRuleDTO();
            ruleDTO.setQuantity(quantity);
            ruleDTO.setCheckLevel(nmd.getLevel());
            NMDIQCRule nmdiqcRule = ruleService.getNMDIQCRule(ruleDTO);
            if (nmd.getPlan() == 1) {
                sampleQuantity = nmdiqcRule.getNormal();
            } else if (nmd.getPlan() == 2) {
                sampleQuantity = nmdiqcRule.getStricture();
            } else {
                sampleQuantity = nmdiqcRule.getRelaxation();
            }
        }

        Collections.shuffle(binInList);
        double finalSampleQuantity = sampleQuantity;
        BinIn binIn = binInList.stream().filter(binin -> binin.getQuantity() >= finalSampleQuantity).collect(Collectors.toList()).get(0);

        IQCSamplePlan iqcSamplePlan = new IQCSamplePlan();
        iqcSamplePlan.setSsccNb(binIn.getSsccNumber());
        iqcSamplePlan.setCell(materialVO.getCell());
        iqcSamplePlan.setMaterialNb(binIn.getMaterialNb());
        iqcSamplePlan.setBinDownCode(binIn.getActualBinCode());
        iqcSamplePlan.setBinDownTime(new Date());
        iqcSamplePlan.setRecommendSampleQuantity(sampleQuantity);
//        iqcSamplePlan.setStatus(binIn.getPlantNb().equals("7751") ? IQCStatusEnum.WAITING_BIN_DOWN.code() : IQCStatusEnum.WARE_SHIFTING.code());
        iqcSamplePlan.setStatus(IQCStatusEnum.WAAITTING_ISSUE.code());
        iqcSamplePlan.setBatchNb(binIn.getBatchNb());
        iqcSamplePlan.setWareCode(binIn.getWareCode());
        iqcSamplePlan.setQuantity(binIn.getQuantity());
        iqcSamplePlan.setExpireDate(binIn.getExpireDate());
        iqcSamplePlan.setPlantNb(binIn.getPlantNb());
        iqcSamplePlan.setUnit(materialVO.getUnit());


        samplePlanMapper.insert(iqcSamplePlan);
        //冻结库存
        LambdaQueryWrapper<Stock> stockQueryWrapper = new LambdaQueryWrapper<>();
        stockQueryWrapper.eq(Stock::getSsccNumber, iqcSamplePlan.getSsccNb());
        stockQueryWrapper.eq(Stock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        stockQueryWrapper.last("limit 1");
        Stock stock = stockService.getOne(stockQueryWrapper);
        stock.setFreezeStock(stock.getTotalStock());
        stock.setAvailableStock(stock.getTotalStock() - stock.getFreezeStock());
        stockService.updateById(stock);


        ArrayList<IQCSamplePlan> list = new ArrayList<>();
        list.add(iqcSamplePlan);
        return list;


    }

    private void updateKanbanWareShiftStatus(Stock stock, String ssccNb) {
        //如果是kanban任务
        LambdaQueryWrapper<MaterialKanban> kanbanQueryWrapper = new LambdaQueryWrapper<>();
        //待下架任务,该kanban状态，待下架
        kanbanQueryWrapper.eq(MaterialKanban::getSsccNumber, ssccNb);
        kanbanQueryWrapper.eq(MaterialKanban::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        kanbanQueryWrapper.ne(MaterialKanban::getStatus, KanbanStatusEnum.FINISH.value());
        kanbanQueryWrapper.ne(MaterialKanban::getStatus, KanbanStatusEnum.CANCEL.value());

        kanbanQueryWrapper.last("limit 1");
        kanbanQueryWrapper.last("for update");
        MaterialKanban materialKanban = kanbanMapper.selectOne(kanbanQueryWrapper);
        if (!Objects.isNull(materialKanban)) {
            materialKanban.setStatus(KanbanStatusEnum.WAITING_BIN_DOWN.value());
            materialKanban.setFactoryCode(stock.getPlantNb());
            materialKanban.setWareCode(stock.getWareCode());
            materialKanban.setAreaCode(stock.getAreaCode());
            materialKanban.setBinCode(stock.getBinCode());
            kanbanMapper.updateById(materialKanban);
            //需要冻结库存
            stock.setFreezeStock(materialKanban.getQuantity());
            stock.setAvailableStock(DoubleMathUtil.doubleMathCalculation(stock.getTotalStock(), stock.getFreezeStock(), "-"));
            stockMapper.updateById(stock);
        }
        //如果是移库任务，需要把状态修改为完成
        LambdaQueryWrapper<WareShift> wareShiftQueryWrapper = new LambdaQueryWrapper<>();
        wareShiftQueryWrapper.eq(WareShift::getSsccNb, ssccNb);
        wareShiftQueryWrapper.eq(WareShift::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        wareShiftQueryWrapper.eq(WareShift::getStatus, KanbanStatusEnum.INNER_BIN_IN.value());
        wareShiftQueryWrapper.last("limit 1");
        wareShiftQueryWrapper.last("for update");
        WareShift wareShift = wareShiftMapper.selectOne(wareShiftQueryWrapper);
        if (!Objects.isNull(wareShift)) {
            wareShift.setStatus(KanbanStatusEnum.FINISH.value());
            wareShift.setTargetPlant(stock.getPlantNb());
            wareShift.setTargetWareCode(stock.getWareCode());
            wareShift.setTargetAreaCode(stock.getAreaCode());
            wareShift.setTargetBinCode(stock.getBinCode());
            wareShiftMapper.updateById(wareShift);
        }

        //如果是转储单任务，需要把状态修改为完成
//        LambdaQueryWrapper<ManualTransferOrder> qw = new LambdaQueryWrapper<>();
//        qw.eq(ManualTransferOrder::getSsccNb, ssccNb);
//        qw.eq(ManualTransferOrder::getStatus, ManuTransStatusEnum.WAITING_BIN_IN.code());
//        qw.eq(ManualTransferOrder::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
//        qw.last("limit 1");
//        qw.last("for update");
//        ManualTransferOrder manualTransferOrder = manualTransferOrderMapper.selectOne(qw);
//        if (!Objects.isNull(manualTransferOrder)) {
//            manualTransferOrder.setStatus(ManuTransStatusEnum.FINISH.code());
//            manualTransferOrderMapper.updateById(manualTransferOrder);
//        }

    }

    @Override
    public int deleteBinInBySscc(String ssccNnumber) {
        LambdaQueryWrapper<BinIn> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BinIn::getSsccNumber,ssccNnumber);
        queryWrapper.eq(BinIn::getDeleteFlag,DeleteFlagStatus.FALSE.getCode());
        BinIn binIn = binInMapper.selectOne(queryWrapper);
        if (binIn.getStatus().equals(BinInStatusEnum.FINISH.value())) {
            throw new ServiceException("该任务已完成上架，不可删除");
        }
        binIn.setDeleteFlag(DeleteFlagStatus.TRUE.getCode());
        return binInMapper.updateById(binIn);
    }

    @Override
    public List<BinInVO> selectProcessingBinVOList(BinInQueryDTO binInQueryDTO) {
        return binInMapper.selectProcessingBinVOList(binInQueryDTO);
    }

    @Override
    public void binDown(String ssccNumber) {
        LambdaQueryWrapper<BinIn> bininQueryMapper = new LambdaQueryWrapper<>();
        bininQueryMapper.eq(BinIn::getSsccNumber, ssccNumber);
        bininQueryMapper.eq(BinIn::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        BinIn binIn = binInMapper.selectOne(bininQueryMapper);
        if (binIn == null || binIn.getStatus().equals(BinInStatusEnum.PROCESSING.value())) {
            throw new ServiceException("上架信息不存在或者正在上架中，不可下架");
        }
        binIn.setUpdateBy(SecurityUtils.getUsername());
        binIn.setUpdateTime(new Date());
        binIn.setDeleteFlag(DeleteFlagStatus.TRUE.getCode());
        //更新库存
        LambdaQueryWrapper<Stock> stockQuertMapper = new LambdaQueryWrapper<>();
        stockQuertMapper.eq(Stock::getSsccNumber, ssccNumber);
        stockQuertMapper.eq(Stock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        Stock stock = stockMapper.selectOne(stockQuertMapper);
        if (stock == null) {
            throw new ServiceException("对应库存不存在");
        }
        stock.setDeleteFlag(DeleteFlagStatus.TRUE.getCode());
        stock.setUpdateBy(SecurityUtils.getUsername());
        stock.setUpdateTime(new Date());
        binInMapper.updateById(binIn);
        stockMapper.updateById(stock);
    }

    @Override
    public BinVO getBinVOByBinCode(String binCode) {
        R<BinVO> binInfoByCodeResult = remoteMasterDataService.getBinInfoByCode(binCode);
        if (StringUtils.isNull(binInfoByCodeResult) || StringUtils.isNull(binInfoByCodeResult.getData())) {
            throw new ServiceException("该库位：" + binCode + " 不存在");
        }

        if (R.FAIL == binInfoByCodeResult.getCode()) {
            throw new ServiceException(binInfoByCodeResult.getMsg());
        }
        return binInfoByCodeResult.getData();
    }

    private void validMaterialBinRule(BinVO binVO, String materialNb) {
        //不在的时候，看actual bin code在不在分配规则内
        R<List<MaterialBinVO>> materialBinVOResullt = remoteMasterDataService.getListByMaterial(materialNb);
        if (StringUtils.isNull(materialBinVOResullt) || StringUtils.isNull(materialBinVOResullt.getData())) {
            throw new ServiceException("该物料：" + materialNb + " 暂无分配规则");
        }

        if (R.FAIL == materialBinVOResullt.getCode()) {
            throw new ServiceException(materialBinVOResullt.getMsg());
        }

        List<MaterialBinVO> materialBinVOS = materialBinVOResullt.getData();
        List<String> frameCodeList = materialBinVOS.stream().map(MaterialBinVO::getFrameTypeCode).collect(Collectors.toList());
        if (StringUtils.isEmpty(frameCodeList) || !frameCodeList.contains(binVO.getFrameTypeCode())) {
            throw new ServiceException("该物料" + materialNb + " 不能分配到" + binVO.getCode());
        }
    }


    private MaterialInVO getMaterialInVO(String mesBarCode) {
        R<MaterialInVO> materialInVOResult = remoteMaterialInService.getByMesBarCode(mesBarCode);
        if (StringUtils.isNull(materialInVOResult) || StringUtils.isNull(materialInVOResult.getData())) {
            throw new ServiceException("物料号：" + MesBarCodeUtil.getMaterialNb(mesBarCode) + ",批次号: " + MesBarCodeUtil.getBatchNb(mesBarCode) + " 未入库");
        }

        if (R.FAIL == materialInVOResult.getCode()) {
            throw new ServiceException(materialInVOResult.getMsg());
        }

        return materialInVOResult.getData();
    }

    @Override
    public BinInVO generateInTaskByMesBarCode(String mesBarCode) {
        String materialNb = MesBarCodeUtil.getMaterialNb(mesBarCode);
        String sscc = MesBarCodeUtil.getSSCC(mesBarCode);
        Double quantity = Double.valueOf(MesBarCodeUtil.getQuantity(mesBarCode));
        String batchNb = MesBarCodeUtil.getBatchNb(mesBarCode);

        BinInVO binInVO = binInMapper.selectBySsccNumber(sscc);
        if (binInVO != null) {
            return binInVO;
        }

        R<List<MaterialBinVO>> materialBinVOResullt = remoteMasterDataService.getListByMaterial(materialNb);
        if (StringUtils.isNull(materialBinVOResullt) || CollectionUtils.isEmpty(materialBinVOResullt.getData())) {
            throw new ServiceException("该物料：" + materialNb + " 分配规则有误");
        }

        if (R.FAIL == materialBinVOResullt.getCode()) {
            throw new ServiceException(materialBinVOResullt.getMsg());
        }
        MaterialVO materialVO = getMaterialVOByCode(materialNb);

        BinAllocationDTO allocationDTO = new BinAllocationDTO();
        allocationDTO.setMesBarCode(mesBarCode);
        //获取托盘
        allocationDTO.setPalletType(materialVO.getPalletType());
        allocationDTO.setWareCode(SecurityUtils.getWareCode());

        //获取托盘详情
        R<Pallet> palletR = remotePalletService.getByType(materialVO.getPalletType());
        if (!palletR.isSuccess()) {
            throw new ServiceException("获取托盘详情失败");
        }
        Pallet pallet = palletR.getData();
        if (pallet == null) {
            throw new ServiceException("未获取到托盘数据");
        }


        BinAllocationVO binAllocationVO = binAssignmentService.getBinAllocationVO(allocationDTO);

        BinVO binVO = getBinVOByBinCode(binAllocationVO.getRecommendBinCode());


        BinIn binIn = new BinIn();
        binIn.setSsccNumber(sscc);
        binIn.setQuantity(quantity);
        binIn.setMaterialNb(materialNb);
        binIn.setBatchNb(batchNb);
        binIn.setExpireDate(MesBarCodeUtil.getExpireDate(mesBarCode));
        binIn.setPalletType(materialVO.getPalletType());
        //设置托盘编码,虚拟托盘直接分配编码
        binIn.setPalletCode(pallet.getIsVirtual().equals(1) ? "V-" + pallet.getVirtualPrefixCode() + "-" + System.currentTimeMillis() : null);
        binIn.setRecommendBinCode(binAllocationVO.getRecommendBinCode());
        binIn.setStatus(BinInStatusEnum.PROCESSING.value());
        binIn.setRecommendFrameId(binVO.getFrameId());
        binIn.setRecommendFrameCode(binVO.getFrameCode());
        binIn.setWareCode(SecurityUtils.getWareCode());

        binIn.setMoveType(MoveTypeEnums.BININ.getCode());
//        binIn.setFromPurchaseOrder(materialInVO.getFromPurchaseOrder());
//        binIn.setPlantNb(materialInVO.getPlantNb());
        binInMapper.insert(binIn);
        return binInMapper.selectBySsccNumber(sscc);

    }

    @Override
    public BinInVO allocateToBinOrArea(String ssccNb, String materialCode, String binCode, String areaCode, Double quantity) {
        BinInVO binInVO = binInMapper.selectBySsccNumber(ssccNb);
        if (binInVO != null) {
            return binInVO;
        }
        StockVO oldStock = stockService.getLastOneBySSCC(ssccNb);
        String sscc = ssccNb;
        String materialNb = materialCode;
        MaterialVO materialVO = getMaterialVOByCode(materialNb);

        quantity = Objects.isNull(quantity) ? oldStock.getTotalStock() : quantity;

        String mesBarCode = MesBarCodeUtil.generateMesBarCode(oldStock.getExpireDate(), sscc, materialNb, oldStock.getBatchNb(), quantity);


        if (Objects.isNull(oldStock)) {
            throw new ServiceException("历史库存为空");
        }

        //获取托盘详情
        R<Pallet> palletR = remotePalletService.getByType(materialVO.getPalletType());
        if (!palletR.isSuccess()) {
            throw new ServiceException("获取托盘详情失败");
        }
        Pallet pallet = palletR.getData();
        if (pallet == null) {
            throw new ServiceException("未获取到托盘数据");
        }

        BinIn binIn = new BinIn();
        binIn.setSsccNumber(sscc);
        binIn.setQuantity(Double.valueOf(MesBarCodeUtil.getQuantity(mesBarCode)));
        binIn.setMaterialNb(materialNb);
        binIn.setBatchNb(oldStock.getBatchNb());
        binIn.setExpireDate(MesBarCodeUtil.getExpireDate(mesBarCode));
        binIn.setPalletType(materialVO.getPalletType());
        //设置托盘编码,虚拟托盘直接分配编码
        binIn.setPalletCode(pallet.getIsVirtual().equals(1) ? "V-" + pallet.getVirtualPrefixCode() + "-" + System.currentTimeMillis() : null);

        if (StringUtils.isNotEmpty(binCode)) {
            BinVO binVO = getBinVOByBinCode(binCode);

            binIn.setRecommendBinCode(binCode);
            binIn.setRecommendFrameId(binVO.getFrameId());
            binIn.setRecommendFrameCode(binVO.getFrameCode());
            binIn.setAreaCode(binVO.getAreaCode());
        } else {
            binIn.setAreaCode(areaCode);
        }


        binIn.setWareCode(SecurityUtils.getWareCode());

        binIn.setStatus(BinInStatusEnum.PROCESSING.value());
        binIn.setMoveType(MoveTypeEnums.SPLIT_IN.getCode());
        binIn.setFromPurchaseOrder(oldStock.getFromPurchaseOrder());
        binIn.setPlantNb(oldStock.getPlantNb());
        binInMapper.insert(binIn);

        return binInMapper.selectBySsccNumber(sscc);
    }

    @Override
    public BinInVO performToAreaType(String mesBarCode, Integer areaType) {
        String sscc = MesBarCodeUtil.getSSCC(mesBarCode);
        String materialNb = MesBarCodeUtil.getMaterialNb(mesBarCode);
        Double quantity = Double.valueOf(MesBarCodeUtil.getQuantity(mesBarCode));
        String batchNb = MesBarCodeUtil.getBatchNb(mesBarCode);
        //根据areaType查询区域
        R<List<AreaVO>> areaListR = remoteMasterDataService.getByWareCode(SecurityUtils.getWareCode());
        if (!areaListR.isSuccess() || areaListR == null) {
            throw new ServiceException("调用主数据服务查询区域列表失败");
        }
        if (StringUtils.isEmpty(areaListR.getData())) {
            throw new ServiceException("没有区域，请维护主数据");
        }
        List<AreaVO> areaVOList = areaListR.getData();
        List<AreaVO> areaList = areaVOList.stream().filter(item -> item.getAreaType() == areaType).collect(Collectors.toList());
        if (StringUtils.isEmpty(areaList)) {
            throw new ServiceException("没有类型为" + AreaTypeEnum.getDescByCode(areaType) + "的区域");
        }

        LambdaQueryWrapper<BinIn> binInQueryWrapper = new LambdaQueryWrapper<>();
        binInQueryWrapper.eq(BinIn::getSsccNumber, sscc);
        binInQueryWrapper.eq(BinIn::getDeleteFlag, DeleteFlagStatus.TRUE.getCode());
        binInQueryWrapper.orderByDesc(BinIn::getUpdateTime);
        binInQueryWrapper.last("limit 1");
        BinIn lastBinIn = this.getOne(binInQueryWrapper);

        AreaVO areaVO = areaList.get(0);


        BinIn binIn = new BinIn();
        binIn.setMaterialNb(materialNb);
        binIn.setBatchNb(batchNb);
        binIn.setExpireDate(MesBarCodeUtil.getExpireDate(mesBarCode));
        binIn.setPlantNb(areaVO.getPlantNb());
        binIn.setWareCode(areaVO.getWareCode());
        binIn.setAreaCode(areaVO.getCode());
        binIn.setActualBinCode(areaVO.getCode());
        binIn.setQuantity(quantity);
        binIn.setStatus(BinInStatusEnum.FINISH.value());
        binIn.setSsccNumber(sscc);
        binIn.setRecommendFrameCode(areaVO.getCode());
        binIn.setMoveType(MoveTypeEnums.IQC_WARE_SHIFT.getCode());
        binIn.setFromPurchaseOrder(lastBinIn.getFromPurchaseOrder());
        binIn.setPalletType(lastBinIn.getPalletType());
        binIn.setPalletCode(lastBinIn.getPalletCode());

        saveOrUpdate(binIn);

        //插入库存
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
        stock.setQualityStatus(QualityStatusEnums.WAITING_QUALITY.getCode());
        stock.setFromPurchaseOrder(binIn.getFromPurchaseOrder());
        stock.setAreaCode(binIn.getAreaCode());
        stock.setPalletCode(binIn.getPalletCode());
        stockMapper.insert(stock);

        return binInMapper.selectBySsccNumber(sscc);
    }

    @Override
    public BinInVO allocateToBinOrArea(String mesBarCode, String binCode, String areaCode) {
        String ssccNb = MesBarCodeUtil.getSSCC(mesBarCode);
        BinInVO binInVO = binInMapper.selectBySsccNumber(ssccNb);
        if (binInVO != null) {
            return binInVO;
        }
        MaterialVO materialVO = getMaterialVOByCode(MesBarCodeUtil.getMaterialNb(mesBarCode));
        //获取托盘详情
        R<Pallet> palletR = remotePalletService.getByType(materialVO.getPalletType());
        if (!palletR.isSuccess()) {
            throw new ServiceException("获取托盘详情失败");
        }
        Pallet pallet = palletR.getData();
        if (pallet == null) {
            throw new ServiceException("未获取到托盘数据");
        }

        BinIn binIn = new BinIn();
        binIn.setSsccNumber(ssccNb);
        binIn.setQuantity(Double.valueOf(MesBarCodeUtil.getQuantity(mesBarCode)));
        binIn.setMaterialNb(MesBarCodeUtil.getMaterialNb(mesBarCode));
        binIn.setBatchNb(MesBarCodeUtil.getBatchNb(mesBarCode));
        binIn.setExpireDate(MesBarCodeUtil.getExpireDate(mesBarCode));
        binIn.setPalletType(materialVO.getPalletType());
        //设置托盘编码,虚拟托盘直接分配编码
        binIn.setPalletCode(pallet.getIsVirtual().equals(1) ? "V-" + pallet.getVirtualPrefixCode() + "-" + System.currentTimeMillis() : null);

        if (StringUtils.isNotEmpty(binCode)) {
            BinVO binVO = getBinVOByBinCode(binCode);

            binIn.setRecommendBinCode(binCode);
            binIn.setRecommendFrameId(binVO.getFrameId());
            binIn.setRecommendFrameCode(binVO.getFrameCode());
            binIn.setAreaCode(binVO.getAreaCode());
        } else {
            binIn.setAreaCode(areaCode);
        }


        binIn.setWareCode(SecurityUtils.getWareCode());

        binIn.setStatus(BinInStatusEnum.PROCESSING.value());
        binIn.setMoveType(MoveTypeEnums.SPLIT_IN.getCode());
//        binIn.setFromPurchaseOrder(oldStock.getFromPurchaseOrder());
//        binIn.setPlantNb(oldStock.getPlantNb());
        binInMapper.insert(binIn);
        return binInMapper.selectBySsccNumber(ssccNb);
    }

    /**
     * 根据历史库存生成上架任务
     *
     * @param ssccNumber
     * @param quantity
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BinInVO generateInTaskByOldStock(String ssccNumber, Double quantity, String wareCode) {
        BinInVO binInVO = binInMapper.selectBySsccNumber(ssccNumber);
        if (binInVO != null) {
            return binInVO;
        }
        StockVO oldStock = stockService.getLastOneBySSCC(ssccNumber);
        String sscc = ssccNumber;
        String materialNb = oldStock.getMaterialNb();
        MaterialVO materialVO = getMaterialVOByCode(materialNb);

        String mesBarCode = MesBarCodeUtil.generateMesBarCode(oldStock.getExpireDate(), sscc, materialNb, oldStock.getBatchNb(), quantity);


        if (Objects.isNull(oldStock)) {
            throw new ServiceException("历史库存为空");
        }
        R<List<MaterialBinVO>> materialBinVOResullt = remoteMasterDataService.getListByMaterial(materialNb);
        if (StringUtils.isNull(materialBinVOResullt) || CollectionUtils.isEmpty(materialBinVOResullt.getData())) {
            throw new ServiceException("该物料：" + materialNb + " 分配规则有误");
        }

        if (R.FAIL == materialBinVOResullt.getCode()) {
            throw new ServiceException(materialBinVOResullt.getMsg());
        }

        BinAllocationDTO allocationDTO = new BinAllocationDTO();
        allocationDTO.setMesBarCode(mesBarCode);
        //获取托盘
        allocationDTO.setPalletType(materialVO.getPalletType());
        allocationDTO.setWareCode(wareCode);

        //获取托盘详情
        R<Pallet> palletR = remotePalletService.getByType(materialVO.getPalletType());
        if (!palletR.isSuccess()) {
            throw new ServiceException("获取托盘详情失败");
        }
        Pallet pallet = palletR.getData();
        if (pallet == null) {
            throw new ServiceException("未获取到托盘数据");
        }


        BinAllocationVO binAllocationVO = binAssignmentService.getBinAllocationVO(allocationDTO);

        BinVO binVO = getBinVOByBinCode(binAllocationVO.getRecommendBinCode());


        BinIn binIn = new BinIn();
        binIn.setSsccNumber(sscc);
        binIn.setQuantity(oldStock.getTotalStock() - quantity);
        binIn.setMaterialNb(materialNb);
        binIn.setBatchNb(oldStock.getBatchNb());
        binIn.setExpireDate(MesBarCodeUtil.getExpireDate(mesBarCode));
        binIn.setPalletType(materialVO.getPalletType());
        //设置托盘编码,虚拟托盘直接分配编码
        binIn.setPalletCode(pallet.getIsVirtual().equals(1) ? "V-" + pallet.getVirtualPrefixCode() + "-" + System.currentTimeMillis() : null);
        binIn.setRecommendBinCode(binAllocationVO.getRecommendBinCode());
        binIn.setStatus(BinInStatusEnum.PROCESSING.value());
        binIn.setRecommendFrameId(binVO.getFrameId());
        binIn.setRecommendFrameCode(binVO.getFrameCode());
        binIn.setWareCode(SecurityUtils.getWareCode());

        binIn.setMoveType(MoveTypeEnums.SPLIT_IN.getCode());
        binIn.setFromPurchaseOrder(oldStock.getFromPurchaseOrder());
        binIn.setPlantNb(oldStock.getPlantNb());
        binInMapper.insert(binIn);
        return binInMapper.selectBySsccNumber(sscc);

    }


}
