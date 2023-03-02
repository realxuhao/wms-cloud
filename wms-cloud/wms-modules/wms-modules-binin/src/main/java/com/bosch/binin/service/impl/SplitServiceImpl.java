package com.bosch.binin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.binin.api.domain.IQCSamplePlan;
import com.bosch.binin.api.domain.SplitRecord;
import com.bosch.binin.api.domain.Stock;
import com.bosch.binin.api.domain.WareShift;
import com.bosch.binin.api.domain.dto.BinInDTO;
import com.bosch.binin.api.domain.dto.SplitPalletDTO;
import com.bosch.binin.api.domain.dto.SplitQuertDTO;
import com.bosch.binin.api.domain.vo.BinInVO;
import com.bosch.binin.api.domain.vo.SplitRecordVO;
import com.bosch.binin.api.enumeration.IQCStatusEnum;
import com.bosch.binin.api.enumeration.KanbanStatusEnum;
import com.bosch.binin.api.enumeration.SplitStatusEnum;
import com.bosch.binin.mapper.SplitMapper;
import com.bosch.binin.service.IBinInService;
import com.bosch.binin.service.ISplitService;
import com.bosch.binin.service.IStockService;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.DoubleMathUtil;
import com.ruoyi.common.core.utils.MesBarCodeUtil;
import com.ruoyi.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-01-17 09:56
 **/
@Service
public class SplitServiceImpl extends ServiceImpl<SplitMapper, SplitRecord> implements ISplitService {

    @Autowired
    private IStockService stockService;

    @Autowired
    private SplitMapper splitMapper;

    @Autowired
    private IBinInService binInService;

    @Override
    public void add(SplitPalletDTO splitPallet) {
        String sourceSsccNb = splitPallet.getSourceSsccNb();
        LambdaQueryWrapper<Stock> stockQueryWrapper = new LambdaQueryWrapper<>();
        stockQueryWrapper.eq(Stock::getSsccNumber, sourceSsccNb);
        stockQueryWrapper.eq(Stock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        stockQueryWrapper.last("limit 1");
        stockQueryWrapper.last("for update");
        Stock sourceStock = stockService.getOne(stockQueryWrapper);
        if (Objects.isNull(sourceStock)) {
            throw new ServiceException("SSCC: " + sourceSsccNb + ",无库存，不可拆托");
        }
        String newMesBarCode = splitPallet.getNewMesBarCode();
        //校验物料号
        if (newMesBarCode.length() == 50) {
            String materialNb = MesBarCodeUtil.getMaterialNb(newMesBarCode);
            if (!materialNb.equals(sourceStock.getMaterialNb())) {
                throw new ServiceException("拆托物料号:" + materialNb + "和源物料号不一致，不可拆托");
            }
        }

        String newSscc = newMesBarCode.length() == 50 ? MesBarCodeUtil.getSSCC(newMesBarCode) : newMesBarCode;
        //校验sscc是否已经存在拆托中
        LambdaQueryWrapper<SplitRecord> splitQueryWrapper = new LambdaQueryWrapper<>();
        splitQueryWrapper.eq(SplitRecord::getSsccNb, newSscc).eq(SplitRecord::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        int count = this.count(splitQueryWrapper);
        if (count > 0) {
            throw new ServiceException("该sscc码已经存在拆托任务");
        }

        //校验拆托数量
        Double splitQuantity = splitPallet.getSplitQuantity();
        if (sourceStock.getAvailableStock() <= splitQuantity) {
            throw new ServiceException("拆托数量不可以大于源库存可用数量");
        }


        SplitRecord splitRecord = new SplitRecord();
        splitRecord.setSplitQuantity(splitQuantity);
        splitRecord.setMaterialNb(sourceStock.getMaterialNb());
        splitRecord.setNewMesBarCode(newMesBarCode);
        splitRecord.setSsccNb(newMesBarCode.length() == 50 ? MesBarCodeUtil.getSSCC(newMesBarCode) : newMesBarCode);
        splitRecord.setSourceSscc(sourceSsccNb);
        splitRecord.setSourceTotalStock(sourceStock.getTotalStock());
        splitRecord.setWareCode(sourceStock.getWareCode());
        save(splitRecord);


        sourceStock.setTotalStock(DoubleMathUtil.doubleMathCalculation(sourceStock.getTotalStock(), splitQuantity, "-"));
        sourceStock.setAvailableStock(DoubleMathUtil.doubleMathCalculation(sourceStock.getTotalStock(), sourceStock.getFreezeStock(), "-"));
        stockService.updateById(sourceStock);
    }

    @Override
    public List<SplitRecordVO> getList(SplitQuertDTO queryDTO) {
        List<SplitRecordVO> list = splitMapper.getList(queryDTO);
        return list;
    }

    @Override
    public BinInVO allocateBin(String mesBarCode, String wareCode) {
        String sscc = MesBarCodeUtil.getSSCC(mesBarCode);
        LambdaQueryWrapper<SplitRecord> qw = new LambdaQueryWrapper<>();
        qw.eq(SplitRecord::getSsccNb, sscc);
        qw.eq(SplitRecord::getStatus, SplitStatusEnum.WAIT_BIN_IN.code());
        qw.eq(SplitRecord::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        qw.last("limit 1");
        qw.last("for update");
        SplitRecord splitRecord = splitMapper.selectOne(qw);

        if (Objects.isNull(splitRecord)) {
            throw new ServiceException("该SSCC码 " + sscc + " 不存在拆托上架任务");
        }
        if (!SplitStatusEnum.WAIT_BIN_IN.code().equals(splitRecord.getStatus())) {
            throw new ServiceException("该SSCC码 " + sscc + "对应任务状态为: " + SplitStatusEnum.getDesc(splitRecord.getStatus()) + " 不可分配库位 ");

        }
        String sourceSscc = splitRecord.getSourceSscc();
        LambdaQueryWrapper<Stock> stockQueryWrapper = new LambdaQueryWrapper<>();

        stockQueryWrapper.eq(Stock::getSsccNumber, sourceSscc);
        stockQueryWrapper.eq(Stock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        stockQueryWrapper.last("limit 1");
        stockQueryWrapper.last("for update");
        Stock sourceStock = stockService.getOne(stockQueryWrapper);
        //分配库位信息
        String newMesBarCode = MesBarCodeUtil.generateMesBarCode(sourceStock.getExpireDate(), sscc, sourceStock.getMaterialNb(), sourceStock.getBatchNb(), splitRecord.getSplitQuantity());
        BinInVO binInVO = binInService.generateInTaskByMesBarCode(newMesBarCode);
        return binInVO;
    }

    @Override
    public void performBinIn(BinInDTO binInDTO) {
        String mesBarCode = binInDTO.getMesBarCode();
        String sscc = MesBarCodeUtil.getSSCC(mesBarCode);
        LambdaQueryWrapper<SplitRecord> iqcQueryWrapper = new LambdaQueryWrapper<>();
        iqcQueryWrapper.eq(SplitRecord::getSsccNb, sscc);
        iqcQueryWrapper.eq(SplitRecord::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        SplitRecord splitRecord = splitMapper.selectOne(iqcQueryWrapper);
        if (splitRecord == null) {
            throw new ServiceException("没有该sscc:" + sscc + "对应的IQC抽样待上架任务");
        }
        if (splitRecord.getStatus() != SplitStatusEnum.WAIT_BIN_IN.code()) {
            throw new ServiceException("sscc:" + sscc + "对应任务状态为:" + IQCStatusEnum.getDesc(splitRecord.getStatus()) + ",不可上架");
        }
        BinInVO binInVO = binInService.performBinIn(binInDTO);
        splitRecord.setStatus(SplitStatusEnum.FINISH.code());
        splitMapper.updateById(splitRecord);
    }
}
