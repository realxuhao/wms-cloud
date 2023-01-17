package com.bosch.binin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.binin.api.domain.SplitRecord;
import com.bosch.binin.api.domain.Stock;
import com.bosch.binin.api.domain.dto.SplitPalletDTO;
import com.bosch.binin.api.domain.dto.SplitQuertDTO;
import com.bosch.binin.api.domain.vo.SplitRecordVO;
import com.bosch.binin.mapper.SplitMapper;
import com.bosch.binin.service.ISplitService;
import com.bosch.binin.service.IStockService;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.DoubleMathUtil;
import com.ruoyi.common.core.utils.MesBarCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        String materialNb = MesBarCodeUtil.getMaterialNb(newMesBarCode);
        if (!materialNb.equals(sourceStock.getMaterialNb())) {
            throw new ServiceException("拆托物料号:" + materialNb + "和源物料号不一致，不可拆托");
        }

        //校验拆托数量
        Double splitQuantity = Double.valueOf(MesBarCodeUtil.getQuantity(newMesBarCode));
        if (sourceStock.getAvailableStock() < splitQuantity) {
            throw new ServiceException("拆托数量不可以大于源库存可用数量");
        }


        SplitRecord splitRecord = new SplitRecord();
        splitRecord.setSplitQuantity(splitQuantity);
        splitRecord.setMaterialNb(materialNb);
        splitRecord.setNewMesBarCode(newMesBarCode);
        splitRecord.setSsccNb(MesBarCodeUtil.getSSCC(newMesBarCode));
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
}
