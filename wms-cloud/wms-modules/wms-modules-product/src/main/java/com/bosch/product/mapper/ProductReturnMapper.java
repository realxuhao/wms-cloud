package com.bosch.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.product.api.domain.ProductReturn;
import com.bosch.product.api.domain.dto.ProductReturnDTO;
import org.apache.ibatis.annotations.Mapper;

/**
* @author GUZ1CGD4
* @description 针对表【material_return(退库表)】的数据库操作Mapper
* @createDate 2022-12-12 11:09:13
* @Entity com.bosch.binin.api.domain.MaterialReturn
*/
@Mapper
public interface ProductReturnMapper extends BaseMapper<ProductReturn> {



}




