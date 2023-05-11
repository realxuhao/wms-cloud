package com.bosch.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.binin.api.RemoteBinInService;
import com.bosch.binin.api.domain.vo.StockVO;
import com.bosch.product.api.domain.RmComparison;
import com.bosch.product.api.domain.dto.RmComparisonDTO;
import com.bosch.product.api.domain.vo.RmComparisonVO;
import com.bosch.product.service.IRmComparisonService;
import com.bosch.product.mapper.RmComparisonMapper;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author GUZ1CGD4
 * @description 针对表【rm_comparison(原材料库存对比)】的数据库操作Service实现
 * @createDate 2023-04-27 13:41:29
 */
@Service
public class IRmComparisonServiceImpl extends ServiceImpl<RmComparisonMapper, RmComparison>
        implements IRmComparisonService {

    @Autowired
    private RemoteBinInService remoteBinInService;

    @Autowired
    private RmComparisonMapper rmComparisonMapper;

    @Override
    public List<RmComparison> getRmComparisonVOList(RmComparisonDTO rmComparisonDTO) {
        //根据查询条件rmComparisonDTO，使用lambda表达式查询数据
        LambdaQueryWrapper<RmComparison> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //根据ssccNumber查询
        if (StringUtils.isNotBlank(rmComparisonDTO.getSsccNumber())) {
            lambdaQueryWrapper.like(RmComparison::getSsccNumber, rmComparisonDTO.getSsccNumber());
        }
        List<RmComparison> rmComparisons = rmComparisonMapper.selectList(lambdaQueryWrapper);

        return rmComparisons;
    }

    //插入数据
    @Override
    public List<RmComparison> insertRmComparison(List<RmComparison> rmComparisons) {
        //获取rmComparisons的ssccs
        List<String> ssccs = rmComparisons.stream().map(RmComparison::getSsccNumber).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(ssccs)) {
            throw new ServiceException("导入数据不能为空");
        }
        //根据ssccs获取stockVOs
        R r = remoteBinInService.listBySSCC(ssccs);
        if (!r.isSuccess()) {
            throw new ServiceException("获取库存信息失败");
        }
        List<StockVO> stockVOs = (List<StockVO>) r.getData();
        //循环rmComparisons，根据ssccNumber获取stockVO，设置stockremainingQty、stockUnitOfMeasure、stockR3StockStatus、stockSapMaterialCode、stockSapBatchNumber
        rmComparisons.forEach(rmComparison -> {
            StockVO stockVO = stockVOs.stream().filter(stock -> stock.getSsccNumber().equals(rmComparison.getSsccNumber())).findFirst().orElse(null);
            if (stockVO != null) {
                rmComparison.setStockRemainingQty(stockVO.getAvailableStock().toString());
                rmComparison.setStockUnitOfMeasure(stockVO.getUnit());
                //rmComparison.setStockR3StockStatus(stockVO.getR3StockStatus());todo
                rmComparison.setStockSapMaterialCode(stockVO.getMaterialNb());
                rmComparison.setStockSapBatchNumber(stockVO.getBatchNb());
            }
        });
        //批量插入
        this.saveBatch(rmComparisons);
        return rmComparisons;
    }

}




