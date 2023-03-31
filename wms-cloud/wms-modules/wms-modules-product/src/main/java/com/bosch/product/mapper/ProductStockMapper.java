package com.bosch.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.binin.api.domain.dto.IQCChangeStatusDTO;
import com.bosch.product.api.domain.ProductStock;
import com.bosch.product.api.domain.dto.ProductIQCManagementQueryDTO;
import com.bosch.product.api.domain.dto.ProductStockQueryDTO;
import com.bosch.product.api.domain.vo.ProductReceiveVO;
import com.bosch.product.api.domain.vo.ProductStockVO;
import com.bosch.product.controller.ProductIQCManagementController;

import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-03-29 13:06
 **/
public interface ProductStockMapper extends BaseMapper<ProductStock> {
    List<ProductStockVO> list(ProductStockQueryDTO stockQueryDTO);

    List<ProductStockVO> selectIQCManagementList(ProductIQCManagementQueryDTO queryDTO);

    int validateStatus(Long id);

    Integer changeStatus(IQCChangeStatusDTO iqcChangeStatusDTO);
}
