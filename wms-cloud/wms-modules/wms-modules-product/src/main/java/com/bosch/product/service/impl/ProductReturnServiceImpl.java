package com.bosch.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.bosch.product.api.domain.ProductReturn;
import com.bosch.product.api.domain.dto.ProductReturnDTO;
import com.bosch.product.api.domain.vo.ProductReturnVO;
import com.bosch.product.mapper.ProductReturnMapper;
import com.bosch.product.service.IProductReturnService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author GUZ1CGD4
 * @description 针对表【material_return(退库表)】的数据库操作Service实现
 * @createDate 2022-12-12 11:09:13
 */
@Service
public class ProductReturnServiceImpl extends ServiceImpl<ProductReturnMapper, ProductReturn>
        implements IProductReturnService {


    @Autowired
    private ProductReturnMapper productReturnMapper;


    @Override
    public boolean addProductReturn(ProductReturnDTO productReturnDTO) {
        return false;
    }

    @Override
    public List<ProductReturnVO> getReturnList(ProductReturnDTO returnDTO) {
        return productReturnMapper.list(returnDTO);
    }
}




