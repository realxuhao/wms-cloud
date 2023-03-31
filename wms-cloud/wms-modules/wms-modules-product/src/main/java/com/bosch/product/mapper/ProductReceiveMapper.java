package com.bosch.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.product.api.domain.ProductReceive;
import com.bosch.product.api.domain.dto.ProductReceiveQueryDTO;
import com.bosch.product.api.domain.vo.ProductReceiveVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-03-29 13:05
 **/
@Mapper
public interface ProductReceiveMapper extends BaseMapper<ProductReceive> {

    List<ProductReceiveVO> list(ProductReceiveQueryDTO queryDTO);



}
