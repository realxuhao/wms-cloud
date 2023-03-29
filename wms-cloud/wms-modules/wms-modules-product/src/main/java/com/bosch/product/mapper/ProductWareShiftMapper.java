package com.bosch.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.product.api.domain.ProductStock;
import com.bosch.product.api.domain.ProductWareShift;
import org.apache.ibatis.annotations.Mapper;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-03-29 16:51
 **/
@Mapper
public interface ProductWareShiftMapper extends BaseMapper<ProductWareShift> {
}
