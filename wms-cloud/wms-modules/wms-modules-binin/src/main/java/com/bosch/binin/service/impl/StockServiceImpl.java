package com.bosch.binin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.binin.api.domain.Stock;
import com.bosch.binin.api.domain.dto.StockQueryDTO;
import com.bosch.binin.api.domain.vo.StockVO;
import com.bosch.binin.mapper.StockMapper;
import com.bosch.binin.service.IStockService;
import com.bosch.binin.utils.BeanConverUtil;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.enums.QualityStatusEnums;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.web.domain.BaseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        List<StockVO> stockVOList = stockMapper.selectStockVOBySortType(stockQuerySTO);
        return stockVOList;
    }

    @Override
    public Double countAvailableStock(StockQueryDTO stockQueryDTO) {
        if (Objects.isNull(stockQueryDTO) || CollectionUtils.isEmpty(stockQueryDTO.getIds())) {
            throw new ServiceException("ids不能为空");
        }
        List<Long> ids = stockQueryDTO.getIds();
        LambdaQueryWrapper<Stock> stockLambdaQueryWrapper = new LambdaQueryWrapper<>();
        stockLambdaQueryWrapper.in(Stock::getId, ids);
        stockLambdaQueryWrapper.eq(Stock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        List<Stock> stockList = stockMapper.selectList(stockLambdaQueryWrapper);
        List<Stock> collect = stockList.stream().filter(item -> item.getFreezeStock() == 0).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(collect) || collect.size() != stockList.size()) {
            throw new ServiceException("库存状态已过期，请刷新页面");
        }

        return stockList.stream().mapToDouble(Stock::getAvailableStock).sum();

    }

    @Override
    public StockVO getOneBySSCC(String ssccs) {

        LambdaQueryWrapper<Stock> stockLambdaQueryWrapper = new LambdaQueryWrapper<>();
        stockLambdaQueryWrapper.eq(Stock::getSsccNumber, ssccs);
        stockLambdaQueryWrapper.eq(Stock::getDeleteFlag, DeleteFlagStatus.TRUE.getCode());
        stockLambdaQueryWrapper.orderByDesc(BaseEntity::getUpdateTime);
        stockLambdaQueryWrapper.last("limit 1");
        Stock stock = stockMapper.selectOne(stockLambdaQueryWrapper);
        StockVO conver = BeanConverUtil.conver(stock, StockVO.class);
        return conver;
    }
}
