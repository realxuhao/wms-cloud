package com.bosch.binin.service.impl;

import com.alibaba.druid.sql.ast.expr.SQLCaseExpr;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.binin.api.StockLog;
import com.bosch.binin.api.domain.MaterialKanban;
import com.bosch.binin.api.domain.Stock;
import com.bosch.binin.api.domain.WareShift;
import com.bosch.binin.api.domain.dto.BinAllocationDTO;
import com.bosch.binin.api.domain.dto.BinInDTO;
import com.bosch.binin.api.domain.dto.BinInTaskDTO;
import com.bosch.binin.api.domain.vo.BinAllocationVO;
import com.bosch.binin.api.domain.vo.FrameRemainVO;
import com.bosch.binin.api.domain.vo.StockVO;
import com.bosch.binin.api.enumeration.BinInStatusEnum;
import com.bosch.binin.api.enumeration.KanbanStatusEnum;
import com.bosch.binin.mapper.*;
import com.bosch.binin.service.IBinAssignmentService;
import com.bosch.binin.service.IStockService;
import com.bosch.masterdata.api.RemoteMasterDataService;
import com.bosch.masterdata.api.RemoteMaterialService;
import com.bosch.masterdata.api.domain.vo.BinVO;
import com.bosch.masterdata.api.domain.vo.MaterialBinVO;
import com.bosch.masterdata.api.domain.vo.MaterialVO;
import com.bosch.storagein.api.RemoteMaterialInService;
import com.bosch.binin.api.domain.BinIn;
import com.bosch.binin.api.domain.dto.BinInQueryDTO;
import com.bosch.binin.api.domain.vo.BinInVO;
import com.bosch.binin.service.IBinInService;
import com.bosch.masterdata.api.domain.Pallet;
import com.bosch.masterdata.api.RemotePalletService;
import com.bosch.storagein.api.domain.vo.MaterialInVO;
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
import org.apache.poi.ss.formula.functions.Odd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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

    @Autowired
    private IStockService stockService;

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

    private MaterialVO getMaterialVOByCode(String materialNb) {
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


            List<String> frameCodeList = materialBinVOS.stream().map(MaterialBinVO::getFrameTypeCode).collect(Collectors.toList());
            if (StringUtils.isEmpty(frameCodeList) || !frameCodeList.contains(actualBinVO.getFrameTypeCode())) {
                throw new ServiceException("该物料" + materialNb + " 不能分配到" + binInDTO.getActualBinCode());
            }

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

        // 更新kanban和移库状态
        updateKanbanWareShiftStatus(stock, sscc);


        return binInMapper.selectBySsccNumber(binIn.getSsccNumber());
    }

    private void updateKanbanWareShiftStatus(Stock stock, String ssccNb) {
        //如果是kanban任务
        LambdaQueryWrapper<MaterialKanban> kanbanQueryWrapper = new LambdaQueryWrapper<>();
        //待下架任务,该kanban状态，待下架
        kanbanQueryWrapper.eq(MaterialKanban::getSsccNumber, ssccNb);
        kanbanQueryWrapper.eq(MaterialKanban::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        kanbanQueryWrapper.ne(MaterialKanban::getStatus, KanbanStatusEnum.FINISH.value());
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

    }

    @Override
    public int deleteBinInById(Long id) {
        BinIn binIn = binInMapper.selectById(id);
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

    private BinVO getBinVOByBinCode(String binCode) {
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
            throw new ServiceException("该物料：" + MesBarCodeUtil.getMaterialNb(mesBarCode) + " 未入库");
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
        StockVO oldStock = stockService.getOneBySSCC(ssccNumber);
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
