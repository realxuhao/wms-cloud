package com.bosch.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.bosch.product.api.domain.ProductReturn;
import com.bosch.product.api.domain.dto.ProductReturnDTO;
import com.bosch.product.mapper.ProductReturnMapper;
import com.bosch.product.service.IProductReturnService;

import org.springframework.stereotype.Service;


/**
 * @author GUZ1CGD4
 * @description 针对表【material_return(退库表)】的数据库操作Service实现
 * @createDate 2022-12-12 11:09:13
 */
@Service
public class ProductReturnServiceImpl extends ServiceImpl<ProductReturnMapper, ProductReturn>
        implements IProductReturnService {


}




