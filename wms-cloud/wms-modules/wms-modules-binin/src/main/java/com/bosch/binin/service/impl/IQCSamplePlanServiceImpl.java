package com.bosch.binin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bosch.binin.api.domain.Stock;
import com.bosch.binin.api.domain.dto.IQCSamplePlanDTO;
import com.bosch.binin.api.domain.dto.IQCSamplePlanQueryDTO;
import com.bosch.binin.api.domain.vo.IQCSamplePlanVO;
import com.bosch.binin.mapper.IQCSamplePlanMapper;
import com.bosch.binin.service.IIQCSamplePlanService;
import com.bosch.binin.service.IStockService;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-02-07 16:15
 **/
@Service
public class IQCSamplePlanServiceImpl implements IIQCSamplePlanService {

    @Autowired
    private IQCSamplePlanMapper samplePlanMapper;

    @Autowired
    private IStockService stockService;

    @Override
    public List<IQCSamplePlanVO> getSamplePlan(IQCSamplePlanQueryDTO dto) {
        return samplePlanMapper.getSamplePlan(dto);
    }

    @Override
    public void manualAdd(List<IQCSamplePlanDTO> dto) {
        if (Objects.isNull(dto)) {
            throw new ServiceException("请选中数据后重试");
        }

        List<String> ssccList = dto.stream().map(IQCSamplePlanDTO::getSsccNb).collect(Collectors.toList());

        LambdaQueryWrapper<Stock> stockQueryWrapper = new LambdaQueryWrapper<>();
        stockQueryWrapper.in(Stock::getSsccNumber, ssccList)
                .eq(Stock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode())
                .eq(Stock::getFreezeStock, Double.valueOf(0));
        List<Stock> stockList = stockService.list(stockQueryWrapper);
        if (Objects.isNull(stockList) || stockList.size() != ssccList.size()) {
            throw new ServiceException("库存数据过期，请刷新后重试");
        }


    }
}
