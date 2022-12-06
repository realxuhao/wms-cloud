package com.bosch.binin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.binin.api.domain.Stock;
import com.bosch.binin.api.domain.WareShift;
import com.bosch.binin.api.domain.dto.AddShiftTaskDTO;
import com.bosch.binin.mapper.WareShiftMapper;
import com.bosch.binin.service.IMaterialKanbanService;
import com.bosch.binin.service.IWareShiftService;
import com.bosch.binin.service.IStockService;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.enums.MoveTypeEnums;
import com.ruoyi.common.core.enums.QualityStatusEnums;
import com.ruoyi.common.core.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

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
                    .build();
            wareShiftList.add(wareShift);
            //更新冻结库存
            item.setFreezeStock(item.getFreezeStock() + item.getAvailableStock());

        });
        //冻结库存，更新状态
        stockService.updateBatchById(stockList);
        return saveBatch(wareShiftList);


    }

    @Override
    public void binDown(String ssccNb) {

    }
}
