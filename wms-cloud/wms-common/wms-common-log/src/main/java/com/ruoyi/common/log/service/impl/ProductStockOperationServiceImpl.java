package com.ruoyi.common.log.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.system.api.domain.ProductStockOperation;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.log.service.IProductStockOperationService;
import com.ruoyi.common.log.mapper.ProductStockOperationMapper;
import com.ruoyi.common.security.utils.SecurityUtils;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
* @author GUZ1CGD4
* @description 针对表【product_stock_operation(库存操作记录表)】的数据库操作Service实现
* @createDate 2023-07-07 14:54:09
*/
@Service
public class ProductStockOperationServiceImpl extends ServiceImpl<ProductStockOperationMapper, ProductStockOperation>
    implements IProductStockOperationService {

    @Autowired
    private ProductStockOperationMapper productStockOperationMapper;
    @Override
    public boolean addProductStockOperationByBean(ProductStockOperation productStockOperation) {
        int insert = productStockOperationMapper.insert(productStockOperation);
        return insert>0;
    }

    @Override
    public boolean addProductStockOperation(String plantNb, Double operationStock, String ssccNumber, String materialNb, String batchNb, Integer operationType) {
        ProductStockOperation productStockOperation=new ProductStockOperation();

        productStockOperation.setPlantNb(plantNb);
        productStockOperation.setSsccNumber(ssccNumber);
        productStockOperation.setMaterialNb(materialNb);
        productStockOperation.setBatchNb(batchNb);
        productStockOperation.setOperationStock(new BigDecimal(operationStock));
        productStockOperation.setOperationType(operationType);
        productStockOperation.setCreateBy(SecurityUtils.getUsername());
        productStockOperation.setCreateTime(new Date());
        int insert = productStockOperationMapper.insert(productStockOperation);
        return insert>0;

    }

    @Override
    public boolean addProductStockOperationBatch(String plantNb, BigDecimal operationStock, Integer operationType, List<ProductStockOperation> list) {
        if (CollectionUtils.isEmpty(list)){
            return false;
        }
        list.forEach(productStockOperation -> {
            if (StringUtils.isEmpty(productStockOperation.getPlantNb())){
                productStockOperation.setPlantNb(plantNb);
            }
         if (productStockOperation.getOperationStock()!=null){
             productStockOperation.setOperationStock(operationStock);
         }
         if (productStockOperation.getOperationType()==null){
             productStockOperation.setOperationType(operationType);
         }

        });

        return false;
    }
}




