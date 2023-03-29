package com.bosch.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.product.api.domain.ProductStock;
import com.bosch.product.api.domain.ProductWareShift;
import com.bosch.product.api.domain.enumeration.ProductWareShiftEnum;
import com.bosch.product.mapper.ProductWareShiftMapper;
import com.bosch.product.service.IProductStockService;
import com.bosch.product.service.IProductWareShiftService;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.enums.MoveTypeEnums;
import com.ruoyi.common.core.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-03-29 16:52
 **/
@Service
public class ProductWareShiftServiceImpl extends ServiceImpl<ProductWareShiftMapper, ProductWareShift> implements IProductWareShiftService {


    @Autowired
    private IProductStockService stockService;

    @Autowired
    private ProductWareShiftMapper wareShiftMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addByStockId(Long stockId) {
        ProductStock productStock = stockService.getById(stockId);
        if (Objects.isNull(productStock)) {
            throw new ServiceException("库存id" + stockId + "对应的库存数据不存在");
        }
        if (!productStock.getFreezeStock().equals(Double.valueOf(0))) {
            throw new ServiceException("该库存有冻结库存，暂时不能移库");
        }

        productStock.setFreezeStock(productStock.getTotalStock());
        productStock.setAvailableStock(Double.valueOf(0));
        stockService.updateById(productStock);

        //新增移库
        ProductWareShift wareShift = ProductWareShift.builder().sourcePlantNb(productStock.getPlantNb())
                .sourceWareCode(productStock.getWareCode())
                .sourceAreaCode(productStock.getAreaCode())
                .sourceBinCode(productStock.getBinCode())
                .quantity(productStock.getTotalStock())
                .moveType(MoveTypeEnums.WARE_SHIFT.getCode())
                .ssccNb(productStock.getSsccNumber())
                .materialNb(productStock.getMaterialNb())
                .batchNb(productStock.getBatchNb())
                .expireDate(productStock.getExpireDate())
                .productionDate(productStock.getProductionDate())
                .unit(productStock.getUnit())
                .status(ProductWareShiftEnum.WAITTING_SHIPPING.code())
                .build();
        wareShiftMapper.insert(wareShift);


    }

    @Override
    public void cancel(Long id) {
        ProductWareShift wareShift = wareShiftMapper.selectById(id);
        if (Objects.isNull(wareShift)) {
            throw new ServiceException("该id:" + id + "对应的移库任务不存在");
        }
        if (!wareShift.getStatus().equals(ProductWareShiftEnum.WAITTING_SHIPPING.code())) {
            throw new ServiceException("该任务对应状态为:" + ProductWareShiftEnum.getDesc(wareShift.getStatus()) + ", 不可取消");
        }

        wareShift.setStatus(ProductWareShiftEnum.CANCEL.code());

        //回滚库存
        LambdaQueryWrapper<ProductStock> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProductStock::getSsccNumber, wareShift.getSsccNb());
        queryWrapper.eq(ProductStock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        queryWrapper.last("limit 1");
        ProductStock productStock = stockService.getOne(queryWrapper);
        if (!Objects.isNull(productStock)) {
            productStock.setFreezeStock(Double.valueOf(0));
            productStock.setAvailableStock(productStock.getTotalStock());
            stockService.updateById(productStock);
        }


    }

    @Override
    public void ship(List<String> ssccList) {

    }
}
