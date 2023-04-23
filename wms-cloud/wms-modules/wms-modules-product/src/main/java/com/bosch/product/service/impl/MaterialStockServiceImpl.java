package com.bosch.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.binin.api.domain.Stock;
import com.bosch.product.api.domain.ProductStock;
import com.bosch.product.api.domain.dto.EditStockDTO;
import com.bosch.product.mapper.MaterialStockMapper;
import com.bosch.product.service.IMaterialStockService;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.MesBarCodeUtil;
import com.ruoyi.common.core.utils.ProductQRCodeUtil;
import org.springframework.stereotype.Service;


/**
 * @author: UWH4SZH
 * @since: 10/19/2022 15:59
 * @description:
 */
@Service
public class MaterialStockServiceImpl extends ServiceImpl<MaterialStockMapper, Stock> implements IMaterialStockService {

    @Override
    public void editStock(EditStockDTO dto) {
        if (dto.getFreezeStock() < dto.getTotalStock()) {
            throw new ServiceException("冻结库存不可以大于总库存");
        }
        String sscc = MesBarCodeUtil.getSSCC(dto.getBarCode());
        LambdaQueryWrapper<Stock> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Stock::getSsccNumber, sscc);
        queryWrapper.eq(Stock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        queryWrapper.last("limit 1");
        Stock stock = this.getOne(queryWrapper);
        stock.setTotalStock(dto.getTotalStock());
        stock.setFreezeStock(dto.getFreezeStock());

        this.updateById(stock);
    }
}
