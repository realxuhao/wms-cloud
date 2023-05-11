package com.bosch.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.binin.api.RemoteBinInService;
import com.bosch.binin.api.domain.vo.StockVO;
import com.bosch.product.api.domain.RmComparison;
import com.bosch.product.api.domain.dto.RmComparisonDTO;
import com.bosch.product.api.domain.enumeration.ComparisonEnum;
import com.bosch.product.api.domain.vo.RmComparisonVO;
import com.bosch.product.service.IRmComparisonService;
import com.bosch.product.mapper.RmComparisonMapper;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.CompareUtils;
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
        //根据materialNumber查询
        if (StringUtils.isNotBlank(rmComparisonDTO.getSapMaterialCode())) {
            lambdaQueryWrapper.like(RmComparison::getSapMaterialCode, rmComparisonDTO.getSapMaterialCode());
        }
        //根据batchNumber查询
        if (StringUtils.isNotBlank(rmComparisonDTO.getSapBatchNumber())) {
            lambdaQueryWrapper.like(RmComparison::getSapBatchNumber, rmComparisonDTO.getSapBatchNumber());
        }
        //根据status查询
        if (StringUtils.isNotNull(rmComparisonDTO.getStatus())) {
            lambdaQueryWrapper.eq(RmComparison::getStatus, rmComparisonDTO.getStatus());
        }
        lambdaQueryWrapper.eq(RmComparison::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
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
        R<List<StockVO>> r = remoteBinInService.listBySSCC(ssccs);
        if (!r.isSuccess()) {
            throw new ServiceException("获取库存信息失败");
        }
        List<StockVO> stockVOs = r.getData();
        //循环rmComparisons，根据ssccNumber获取stockVO，设置stockremainingQty、stockUnitOfMeasure、stockR3StockStatus、stockSapMaterialCode、stockSapBatchNumber
        rmComparisons.forEach(rmComparison -> {
            StockVO stockVO = stockVOs.stream().filter(stock -> stock.getSsccNumber().equals(rmComparison.getSsccNumber())).findFirst().orElse(null);
            rmComparison.setStatus(ComparisonEnum.DIFF.code());
            if (stockVO != null) {
                rmComparison.setStockRemainingQty(stockVO.getAvailableStock().toString());
                rmComparison.setStockUnitOfMeasure(stockVO.getUnit());
                rmComparison.setStockR3StockStatus(stockVO.getQualityStatus());
                rmComparison.setStockSapMaterialCode(stockVO.getMaterialNb());
                rmComparison.setStockSapBatchNumber(stockVO.getBatchNb());

                //如果rmComparison和stockVO的上面五个字段都相同，则设置status为相同
                if (CompareUtils.isEqual(rmComparison.getRemainingQty(),stockVO.getAvailableStock().toString())
                        && rmComparison.getUnitOfMeasure().equals(stockVO.getUnit())
                        && rmComparison.getR3StockStatus().equals(stockVO.getQualityStatus())
                        && rmComparison.getSapMaterialCode().equals(stockVO.getMaterialNb())
                        && rmComparison.getSapBatchNumber().equals(stockVO.getBatchNb())) {
                    rmComparison.setStatus(ComparisonEnum.SAME.code());
                }
            }
        });
        //批量插入
        this.saveBatch(rmComparisons);
        return rmComparisons;
    }

}




