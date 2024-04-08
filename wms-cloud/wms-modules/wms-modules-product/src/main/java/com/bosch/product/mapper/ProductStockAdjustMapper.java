package com.bosch.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.binin.api.domain.StockAdjust;
import com.bosch.product.api.domain.ProductStockAdjust;
import com.bosch.product.api.domain.dto.ProductStockQueryDTO;
import com.bosch.product.api.domain.vo.ProductStockAdjustVO;
import com.bosch.product.api.domain.vo.ProductStockVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-06-01 15:06
 **/
@Mapper
@Repository("productStockAdjustMapper")
public interface ProductStockAdjustMapper extends BaseMapper<ProductStockAdjust> {
    List<ProductStockAdjustVO> getStockAdjustVOList(ProductStockQueryDTO stockQueryDTO);

}
