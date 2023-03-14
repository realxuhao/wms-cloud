package com.bosch.binin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.binin.api.domain.Stock;
import com.bosch.binin.api.domain.dto.IQCChangeStatusDTO;
import com.bosch.binin.api.domain.dto.IQCManagementQueryDTO;
import com.bosch.binin.api.domain.dto.StockQueryDTO;
import com.bosch.binin.api.domain.vo.StockVO;
import com.bosch.binin.mapper.StockMapper;
import com.bosch.binin.service.IStockService;
import com.bosch.binin.utils.BeanConverUtil;
import com.bosch.masterdata.api.domain.dto.IQCDTO;
import com.bosch.masterdata.api.domain.vo.IQCVO;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.enums.QualityStatusEnums;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.web.domain.BaseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
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
    public List<StockVO> selectIQCManagementList(IQCManagementQueryDTO iqcManagementQueryDTO) {

        return stockMapper.selectIQCManagementList(iqcManagementQueryDTO);
    }

    @Override
    public boolean validateStatus(Long id) {
        return stockMapper.validateStatus(id) == 1;
    }

    @Override
    public Integer changeStatus(IQCChangeStatusDTO iqcChangeStatusDTO) {
//        Integer i = new Integer(0);
//        if (iqcChangeStatusDTO.getType().equals(0)) {
//            i = stockMapper.changeStatus(iqcChangeStatusDTO);
//        } else {
//            LambdaQueryWrapper<Stock> queryWrapper=new LambdaQueryWrapper<>();
//            queryWrapper.eq(Stock::getId,iqcChangeStatusDTO.getId());
//
//            stockMapper.selectOne(queryWrapper)
//            i = stockMapper.changeStatus(iqcChangeStatusDTO);
//        }
        Integer i = stockMapper.changeStatus(iqcChangeStatusDTO);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<IQCVO> excelChangeStatus(List<IQCDTO> list) {
        List<IQCVO> result = new ArrayList<>();
        // 定义每个线程处理的数据量
        int batchSize = 100;
        // 计算需要启动的线程数
        int threadCount = (int) Math.ceil((double) list.size() / batchSize);
        // 创建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        // 使用循环分配数据给每个线程处理
        for (int i = 0; i < threadCount; i++) {
            int start = i * batchSize;
            int end = Math.min(start + batchSize, list.size());
            // 创建一个线程处理当前批次的数据
            executorService.execute(() -> {
                // 获取当前线程处理的数据
                List<IQCDTO> batchList = list.subList(start, end);
                // 执行批量更新操作
                for (IQCDTO iqcdto : batchList) {
                    LambdaUpdateWrapper<Stock> wrapper = new LambdaUpdateWrapper<>();
                    wrapper.eq(Stock::getSsccNumber, iqcdto.getSSCCNumber());
                    wrapper.eq(Stock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
                    wrapper.set(Stock::getQualityStatus, iqcdto.getFinalSAPStatus());
                    wrapper.set(Stock::getChangeStatus, 1);
                    boolean update = this.update(wrapper);
                    IQCVO conver = BeanConverUtil.conver(iqcdto, IQCVO.class);
                    conver.setStatus(update ? 0 : 1);
                    //result.add(conver);
                    synchronized(result) { // 确保线程安全
                        result.add(conver); // 添加到结果列表
                    }
                }
            });
        }

// 关闭线程池
        executorService.shutdown();
        try {
            // 等待所有线程执行完毕
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            // 处理线程被中断的异常
            e.printStackTrace();
        }
        return result;
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
        List<Stock> collect =
                stockList.stream().filter(item -> item.getFreezeStock() == 0).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(collect) || collect.size() != stockList.size()) {
            throw new ServiceException("库存状态已过期，请刷新页面");
        }

        return stockList.stream().mapToDouble(Stock::getAvailableStock).sum();

    }

    @Override
    public StockVO getLastOneBySSCC(String ssccs) {

        LambdaQueryWrapper<Stock> stockLambdaQueryWrapper = new LambdaQueryWrapper<>();
        stockLambdaQueryWrapper.eq(Stock::getSsccNumber, ssccs);
        stockLambdaQueryWrapper.eq(Stock::getDeleteFlag, DeleteFlagStatus.TRUE.getCode());
        stockLambdaQueryWrapper.orderByDesc(BaseEntity::getUpdateTime);
        stockLambdaQueryWrapper.last("limit 1");
        Stock stock = stockMapper.selectOne(stockLambdaQueryWrapper);
        StockVO conver = BeanConverUtil.conver(stock, StockVO.class);
        return conver;
    }

    @Override
    public Stock getAvailablesStockBySscc(String sscc) {
        LambdaQueryWrapper<Stock> stockLambdaQueryWrapper = new LambdaQueryWrapper<>();
        stockLambdaQueryWrapper.eq(Stock::getSsccNumber, sscc);
        stockLambdaQueryWrapper.eq(Stock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        stockLambdaQueryWrapper.last("limit 1");
        Stock stock = stockMapper.selectOne(stockLambdaQueryWrapper);
        return stock;
    }

    @Override
    public Stock getOneStock(String sscc) {
        LambdaQueryWrapper<Stock> stockLambdaQueryWrapper = new LambdaQueryWrapper<>();
        stockLambdaQueryWrapper.eq(Stock::getSsccNumber, sscc);
        stockLambdaQueryWrapper.last("limit 1");
        Stock stock = stockMapper.selectOne(stockLambdaQueryWrapper);
        return stock;
    }

    @Override
    public List<StockVO> getBinStockLog(String binCode) {
        return stockMapper.getBinStockLog(binCode);
    }
}
