package com.bosch.binin.service.impl;

import com.alibaba.druid.sql.ast.statement.SQLIfStatement;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.binin.api.domain.*;
import com.bosch.binin.api.domain.dto.AddManualTransDTO;
import com.bosch.binin.api.domain.dto.ManualBinInDTO;
import com.bosch.binin.api.domain.dto.ManualTransQueryDTO;
import com.bosch.binin.api.domain.vo.BinInVO;
import com.bosch.binin.api.domain.vo.ManualTransferOrderVO;
import com.bosch.binin.api.enumeration.BinInStatusEnum;
import com.bosch.binin.api.enumeration.ManuTransStatusEnum;
import com.bosch.binin.api.enumeration.MaterialTransTypeEnum;
import com.bosch.binin.mapper.BinInMapper;
import com.bosch.binin.mapper.ManualTransferOrderMapper;
import com.bosch.binin.mapper.StockMapper;
import com.bosch.binin.service.IBinInService;
import com.bosch.binin.service.IManualTransferOrderService;
import com.bosch.binin.service.IStockService;
import com.bosch.masterdata.api.domain.vo.BinVO;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.enums.MoveTypeEnums;
import com.ruoyi.common.core.enums.QualityStatusEnums;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.DoubleMathUtil;
import com.ruoyi.common.core.utils.MesBarCodeUtil;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-12-20 13:48
 **/
@Service
public class ManualTransferOrderServiceImpl extends ServiceImpl<ManualTransferOrderMapper, ManualTransferOrder> implements IManualTransferOrderService {

    @Autowired
    private ManualTransferOrderMapper manualTransferOrderMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private IBinInService binInService;

    @Autowired
    private BinInMapper binInMapper;

    @Autowired
    private IStockService stockService;

    @Override
    public List<ManualTransferOrderVO> list(ManualTransQueryDTO queryDTO) {
        return manualTransferOrderMapper.list(queryDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean add(AddManualTransDTO dto) {
        List<Long> idList = dto.getIdList();
//        String targetBinCode = dto.getTargetBinCode();
        String targetAreaCode = dto.getTargetAreaCode();
        if (CollectionUtils.isEmpty(idList)) {
            throw new ServiceException("请选择库存后重试");
        }
        LambdaQueryWrapper<Stock> stockLambdaQueryWrapper = new LambdaQueryWrapper<>();
        stockLambdaQueryWrapper.in(Stock::getId, idList);
        stockLambdaQueryWrapper.eq(Stock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        stockLambdaQueryWrapper.eq(Stock::getFreezeStock, Double.valueOf(0));
        List<Stock> stockList = stockMapper.selectList(stockLambdaQueryWrapper);
        if (CollectionUtils.isEmpty(stockList) || stockList.size() != idList.size()) {
            throw new ServiceException("库存状态过期，请刷新后重新选择");
        }
        List<ManualTransferOrder> list = new ArrayList<>();
        stockList.stream().forEach(item -> {
            ManualTransferOrder manualTransferOrder = new ManualTransferOrder();
            manualTransferOrder.setSourcePlantNb(item.getPlantNb());
            manualTransferOrder.setSourceWareCode(item.getWareCode());
            manualTransferOrder.setSourceAreaCode(item.getAreaCode());
            manualTransferOrder.setSourceBinCode(item.getBinCode());
            manualTransferOrder.setTargetAreaCode(targetAreaCode);
            manualTransferOrder.setSsccNb(item.getSsccNumber());
            manualTransferOrder.setMaterialNb(item.getMaterialNb());
            manualTransferOrder.setType(dto.getNormalType());
            manualTransferOrder.setMoveType(MoveTypeEnums.IN_TRANSFER.getCode());
            manualTransferOrder.setStatus(ManuTransStatusEnum.WAITING_ISSUE.code());
            list.add(manualTransferOrder);

            //stock更新
            item.setFreezeStock(item.getTotalStock());
            item.setAvailableStock(Double.valueOf(0));
            if (manualTransferOrder.getType() == MaterialTransTypeEnum.AB_NORMAL.code()) {
                item.setQualityStatus(QualityStatusEnums.WAITING_QUALITY.getCode());
            } else {
                item.setQualityStatus(QualityStatusEnums.USE.getCode());
            }
        });

        stockService.updateBatchById(stockList);


        return saveBatch(list);
    }

    @Override
    public BinInVO generateBinInJob(String mesBarCode, String wareCode) {
        String sscc = MesBarCodeUtil.getSSCC(mesBarCode);
        String materialCode = MesBarCodeUtil.getMaterialNb(mesBarCode);
        LambdaQueryWrapper<ManualTransferOrder> qw = new LambdaQueryWrapper<>();
        qw.eq(ManualTransferOrder::getSsccNb, sscc);
//        qw.eq(ManualTransferOrder::getStatus, ManuTransStatusEnum.WAITING_BIN_IN.code());
        qw.eq(ManualTransferOrder::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        qw.last("limit 1");
        qw.last("for update");
        ManualTransferOrder manualTransferOrder = manualTransferOrderMapper.selectOne(qw);

        if (Objects.isNull(manualTransferOrder)) {
            throw new ServiceException("该SSCC码 " + sscc + " 不存在转储任务或者状态已过期");
        }
        if (!ManuTransStatusEnum.WAITING_BIN_IN.code().equals(manualTransferOrder.getStatus())) {
            throw new ServiceException("该SSCC码 " + sscc + "对应任务状态为: " + ManuTransStatusEnum.getDesc(String.valueOf(manualTransferOrder.getStatus())) + " 不可分配库位 ");

        }
        //分配库位信息

//        if (manualTransferOrder.getType() == MaterialTransTypeEnum.NORMAL.code()) {
//            binInService.allocateToBinOrArea(sscc, materialCode, manualTransferOrder.getTargetBinCode(), null);
//        } else {
        binInService.allocateToBinOrArea(sscc, materialCode, null, manualTransferOrder.getTargetAreaCode(), null);
//        }
        return binInService.getByMesBarCode(mesBarCode);
    }

    @Override
    public void issueJob(Long[] ids) {
        List<Long> idList = Arrays.asList(ids);
        if (CollectionUtils.isEmpty(idList)) {
            throw new ServiceException("任务为空，请选择任务后重试");
        }
        LambdaQueryWrapper<ManualTransferOrder> kanbanLambdaQueryWrapper = new LambdaQueryWrapper<>();
        kanbanLambdaQueryWrapper.in(ManualTransferOrder::getId, idList);
        kanbanLambdaQueryWrapper.eq(ManualTransferOrder::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        kanbanLambdaQueryWrapper.eq(ManualTransferOrder::getStatus, ManuTransStatusEnum.WAITING_ISSUE.code());

        List<ManualTransferOrder> manualTransferOrderList = manualTransferOrderMapper.selectList(kanbanLambdaQueryWrapper);

        if (CollectionUtils.isEmpty(manualTransferOrderList) || manualTransferOrderList.size() != idList.size()) {
            throw new ServiceException("数据已过期，请刷新后重新选择");
        }
        manualTransferOrderList.stream().forEach(item -> {
            item.setStatus(ManuTransStatusEnum.WAITING_BIN_DOWN.code());
        });

        updateBatchById(manualTransferOrderList);


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancel(String[] ssccNumbers) {
        List<String> ssccNumberList = Arrays.asList(ssccNumbers);
        if (CollectionUtils.isEmpty(ssccNumberList)) {
            throw new ServiceException("任务为空，请选择任务后重试");
        }
        LambdaQueryWrapper<ManualTransferOrder> kanbanLambdaQueryWrapper = new LambdaQueryWrapper<>();
        kanbanLambdaQueryWrapper.in(ManualTransferOrder::getSsccNb, ssccNumberList);
        kanbanLambdaQueryWrapper.eq(ManualTransferOrder::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        kanbanLambdaQueryWrapper.eq(ManualTransferOrder::getStatus, ManuTransStatusEnum.WAITING_ISSUE.code());

        List<ManualTransferOrder> manualTransferOrderList = manualTransferOrderMapper.selectList(kanbanLambdaQueryWrapper);
        if (CollectionUtils.isEmpty(manualTransferOrderList) || manualTransferOrderList.size() != ssccNumberList.size()) {
            throw new ServiceException("数据已过期，请刷新后重新选择");
        }
        manualTransferOrderList.stream().forEach(item -> item.setStatus(ManuTransStatusEnum.CANCEL.code()));
        updateBatchById(manualTransferOrderList);

        LambdaQueryWrapper<Stock> stockQueryWrapper = new LambdaQueryWrapper<>();
        stockQueryWrapper.in(Stock::getSsccNumber, ssccNumberList);
        stockQueryWrapper.eq(Stock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        stockQueryWrapper.last("and total_stock=freeze_stock");
        List<Stock> stockList = stockMapper.selectList(stockQueryWrapper);
        stockList.stream().forEach(item -> {
            item.setAvailableStock(item.getTotalStock());
            item.setFreezeStock(DoubleMathUtil.doubleMathCalculation(item.getTotalStock(), item.getAvailableStock(), "-"));
        });
        stockService.updateBatchById(stockList);

    }

    @Override
    public void binDown(String mesBarCode) {
        String ssccNb = MesBarCodeUtil.getSSCC(mesBarCode);
        LambdaQueryWrapper<ManualTransferOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ManualTransferOrder::getSsccNb, ssccNb);
        queryWrapper.eq(ManualTransferOrder::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        queryWrapper.ne(ManualTransferOrder::getStatus, ManuTransStatusEnum.CANCEL.code());
        queryWrapper.last("for update");
        ManualTransferOrder manualTransferOrder = manualTransferOrderMapper.selectOne(queryWrapper);
        if (Objects.isNull(manualTransferOrder)) {
            throw new ServiceException("该SSCC码 " + ssccNb + " 不存在仓内转储任务");
        }
        if (!ManuTransStatusEnum.WAITING_BIN_DOWN.code().equals(manualTransferOrder.getStatus())) {
            throw new ServiceException("该SSCC码 " + ssccNb + "对应任务状态为: " + ManuTransStatusEnum.getDesc(String.valueOf(manualTransferOrder.getStatus())) + " 不可下架 ");

        }


        //状态修改为外库已下架
        manualTransferOrder.setStatus(ManuTransStatusEnum.WAITING_BIN_IN.code());
        manualTransferOrderMapper.updateById(manualTransferOrder);

        //执行下架
        binInService.binDown(ssccNb);
    }

    @Override
    public BinInVO performBinIn(ManualBinInDTO binInDTO) {
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

        String ssccNb = MesBarCodeUtil.getSSCC(mesBarCode);
        LambdaQueryWrapper<ManualTransferOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ManualTransferOrder::getSsccNb, ssccNb);
        queryWrapper.eq(ManualTransferOrder::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        queryWrapper.ne(ManualTransferOrder::getStatus, ManuTransStatusEnum.CANCEL.code());
        queryWrapper.last("for update");
        ManualTransferOrder manualTransferOrder = manualTransferOrderMapper.selectOne(queryWrapper);


        if (manualTransferOrder.getType() == MaterialTransTypeEnum.AB_NORMAL.code()) {
            //异常入库

            binIn.setUpdateBy(SecurityUtils.getUsername());
            binIn.setStatus(BinInStatusEnum.FINISH.value());
            binIn.setUpdateTime(new Date());
            binIn.setAreaCode(binInDTO.getActualCode());
            if (StringUtils.isEmpty(binIn.getPalletCode())) {
                binIn.setPalletCode(binInDTO.getPalletCode());
            }
            binInService.updateById(binIn);
        } else {
            // 正常入库
//            BinVO actualBinVO = binInService.getBinVOByBinCode(binInDTO.getActualCode());
            //校验库位是否是存取区的
            BinVO actualBinVO = binInService.getBinVOByBinCode(binInDTO.getActualCode());

            if (!binIn.getAreaCode().equals(actualBinVO.getAreaCode())) {
                throw new ServiceException("当前库位：" + binInDTO.getActualCode() + " 不属于" + binIn.getAreaCode() + "存储区");
            }
            binIn.setActualBinCode(binInDTO.getActualCode());
            binIn.setActualFrameCode(actualBinVO.getFrameCode());
            binIn.setActualFrameId(actualBinVO.getFrameId());
            binIn.setUpdateBy(SecurityUtils.getUsername());
            binIn.setStatus(BinInStatusEnum.FINISH.value());
            binIn.setUpdateTime(new Date());
            binIn.setAreaCode(actualBinVO.getAreaCode());
            if (StringUtils.isEmpty(binIn.getPalletCode())) {
                binIn.setPalletCode(binInDTO.getPalletCode());
            }
            binInService.saveOrUpdate(binIn);

        }

        //更新库存
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

        //更新仓内转储状态为完成
        manualTransferOrder.setStatus(ManuTransStatusEnum.FINISH.code());
        manualTransferOrderMapper.updateById(manualTransferOrder);



        return binInMapper.selectBySsccNumber(binIn.getSsccNumber());
    }

    @Override
    public ManualTransferOrder info(String mesBarCode) {
        String sscc = MesBarCodeUtil.getSSCC(mesBarCode);
        String materialNb = MesBarCodeUtil.getMaterialNb(mesBarCode);
        LambdaQueryWrapper<ManualTransferOrder> qw = new LambdaQueryWrapper<>();
        qw.eq(ManualTransferOrder::getSsccNb, sscc);
        qw.eq(ManualTransferOrder::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        qw.ne(ManualTransferOrder::getStatus, ManuTransStatusEnum.CANCEL.code());
        qw.last("limit 1");
        qw.last("for update");
        manualTransferOrderMapper.selectOne(qw);
        return manualTransferOrderMapper.selectOne(qw);
    }
}
