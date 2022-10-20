package com.bosch.binin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.binin.api.domain.Stock;
import com.bosch.binin.mapper.BinStockMapper;
import com.bosch.binin.service.IBinStockService;
import org.springframework.stereotype.Service;

/**
 * @author: UWH4SZH
 * @since: 10/19/2022 15:59
 * @description:
 */
@Service
public class BinStockServiceImpl extends ServiceImpl<BinStockMapper, Stock> implements IBinStockService {
}
