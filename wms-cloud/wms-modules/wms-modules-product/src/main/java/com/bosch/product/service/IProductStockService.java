package com.bosch.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.product.api.domain.ProductReceive;
import com.bosch.product.api.domain.ProductStock;
import com.bosch.product.api.domain.dto.ProductStockQueryDTO;
import com.bosch.product.api.domain.vo.ProductReceiveVO;
import com.bosch.product.api.domain.vo.ProductStockVO;

import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-03-29 11:21
 **/
public interface IProductStockService extends IService<ProductStock> {

    void generateStockByReceive(ProductReceive receive);


    List<ProductStockVO> list(ProductStockQueryDTO stockQueryDTO);
}
