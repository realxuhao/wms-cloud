package com.bosch.binin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.binin.api.domain.Stock;
import com.bosch.binin.api.domain.dto.StockQueryDTO;
import com.bosch.binin.api.domain.vo.StockVO;
import com.bosch.binin.mapper.StockMapper;
import com.bosch.binin.service.IStockService;
import com.ruoyi.common.core.enums.QualityStatusEnums;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: UWH4SZH
 * @since: 10/19/2022 15:59
 * @description:
 */
@Service
public class StockServiceImpl extends ServiceImpl<StockMapper, Stock> implements IStockService {

    @Autowired
    private StockMapper stockMapper;

    @Override
    public List<StockVO> selectStockVOList(StockQueryDTO stockQueryDTO) {

        return stockMapper.selectStockVOList(stockQueryDTO);
    }

    @Override
    public List<StockVO> selectStockVOBySortType(StockQueryDTO stockQuerySTO) {
        List<StockVO> stockVOList=stockMapper.selectStockVOBySortType(stockQuerySTO);
        return stockVOList;
    }
}
