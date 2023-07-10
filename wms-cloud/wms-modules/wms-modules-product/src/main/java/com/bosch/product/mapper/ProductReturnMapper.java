package com.bosch.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.binin.api.domain.dto.MaterialReturnQueryDTO;
import com.bosch.binin.api.domain.vo.MaterialReturnVO;
import com.bosch.product.api.domain.ProductReturn;
import com.bosch.product.api.domain.dto.ProductReturnDTO;
import com.bosch.product.api.domain.vo.ProductReturnVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author GUZ1CGD4
* @description 针对表【material_return(退库表)】的数据库操作Mapper
* @createDate 2022-12-12 11:09:13
* @Entity com.bosch.binin.api.domain.MaterialReturn
*/
@Mapper
public interface ProductReturnMapper extends BaseMapper<ProductReturn> {


    List<ProductReturnVO> list(ProductReturnDTO queryDTO);




}




