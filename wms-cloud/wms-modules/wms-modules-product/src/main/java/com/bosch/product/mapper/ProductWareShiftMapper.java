package com.bosch.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.binin.api.domain.dto.WareShiftQueryDTO;
import com.bosch.product.api.domain.ProductStock;
import com.bosch.product.api.domain.ProductWareShift;
import com.bosch.product.api.domain.dto.ProductWareShiftQueryDTO;
import com.bosch.product.api.domain.vo.ProductWareShiftVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-03-29 16:51
 **/
@Mapper
public interface ProductWareShiftMapper extends BaseMapper<ProductWareShift> {
    List<ProductWareShiftVO> list(ProductWareShiftQueryDTO queryDTO);
}
