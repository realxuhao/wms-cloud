package com.ruoyi.common.log.service;

import com.bosch.system.api.domain.ProductStockOperation;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;

/**
* @author GUZ1CGD4
* @description 针对表【product_stock_operation(库存操作记录表)】的数据库操作Service
* @createDate 2023-07-07 14:54:09
*/
public interface IProductStockOperationService extends IService<ProductStockOperation> {

    /**
     * 新增库存操作记录
     * @param productStockOperation
     * @return
     */
    boolean addProductStockOperation(ProductStockOperation productStockOperation);
    /**
     * 批量新增库存操作记录
     */
    boolean addProductStockOperationBatch(String plantNb, BigDecimal operationStock, String operationType, List<ProductStockOperation> list);
}
