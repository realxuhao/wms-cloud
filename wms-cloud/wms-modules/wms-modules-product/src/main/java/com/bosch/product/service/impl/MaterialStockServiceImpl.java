package com.bosch.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.binin.api.domain.Stock;
import com.bosch.product.mapper.MaterialStockMapper;
import com.bosch.product.service.IMaterialStockService;
import org.springframework.stereotype.Service;


/**
 * @author: UWH4SZH
 * @since: 10/19/2022 15:59
 * @description:
 */
@Service
public class MaterialStockServiceImpl extends ServiceImpl<MaterialStockMapper, Stock> implements IMaterialStockService {

}
