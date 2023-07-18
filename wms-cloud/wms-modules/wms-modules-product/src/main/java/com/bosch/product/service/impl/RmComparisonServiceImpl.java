package com.bosch.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.binin.api.RemoteBinInService;
import com.bosch.binin.api.domain.vo.StockVO;
import com.bosch.product.api.domain.ProComparison;
import com.bosch.product.api.domain.RmComparison;
import com.bosch.product.api.domain.dto.RmComparisonDTO;
import com.bosch.product.api.domain.enumeration.ComparisonEnum;
import com.bosch.product.api.domain.vo.ProductStockVO;
import com.bosch.product.mapper.ProComparisonMapper;
import com.bosch.product.mapper.ProductStockMapper;
import com.bosch.product.service.IProComparisonService;
import com.bosch.product.service.IRmComparisonService;
import com.bosch.product.mapper.RmComparisonMapper;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.CompareUtils;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author GUZ1CGD4
 * @description 针对表【rm_comparison(原材料库存对比)】的数据库操作Service实现
 * @createDate 2023-04-27 13:41:29
 */
@Service
public class RmComparisonServiceImpl extends ServiceImpl<RmComparisonMapper, RmComparison>
        implements IRmComparisonService {

    @Autowired
    private RemoteBinInService remoteBinInService;

    @Autowired
    private RmComparisonMapper rmComparisonMapper;

    @Autowired
    private ProductStockMapper productStockMapper;

    @Autowired
    private ProComparisonMapper proComparisonMapper;

    @Autowired
    private IProComparisonService proComparisonService;

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

        //循环rmComparisons，根据ssccNumber获取stockVO，设置stockremainingQty、stockUnitOfMeasure、stockR3StockStatus
        // 、stockSapMaterialCode、stockSapBatchNumber
        rmComparisons.forEach(rmComparison -> {
            try {
                StockVO stockVO =
                        stockVOs.stream().filter(stock -> stock.getSsccNumber().equals(rmComparison.getSsccNumber())).findFirst().orElse(null);
                rmComparison.setStatus(ComparisonEnum.DIFF.code());
                if (stockVO != null) {

                    rmComparison.setStockRemainingQty(stockVO.getTotalStock() == null ? "0" :
                            stockVO.getTotalStock().toString());
                    rmComparison.setStockUnitOfMeasure(stockVO.getUnit());
                    rmComparison.setStockR3StockStatus(stockVO.getQualityStatus());
                    rmComparison.setStockSapMaterialCode(stockVO.getMaterialNb());
                    rmComparison.setStockSapBatchNumber(stockVO.getBatchNb());

                    boolean numeric = StringUtils.isNumeric(rmComparison.getRemainingQty());
                    if(!numeric){
                        throw  new ServiceException(rmComparison.getSsccNumber()+" 的remainingQty非数字数据:"+rmComparison.getRemainingQty());
                    }

                    //如果rmComparison和stockVO的上面五个字段都相同，则设置status为相同
                    if (CompareUtils.isEqual(rmComparison.getRemainingQty(), stockVO.getTotalStock() == null ? "0"
                            : stockVO.getTotalStock().toString())
                            && rmComparison.getUnitOfMeasure().equals(stockVO.getUnit())
                            && rmComparison.getR3StockStatus().equals(stockVO.getQualityStatus())
                            && rmComparison.getSapMaterialCode().equals(stockVO.getMaterialNb())
                            && rmComparison.getSapBatchNumber().equals(stockVO.getBatchNb())) {
                        rmComparison.setStatus(ComparisonEnum.SAME.code());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                StackTraceElement stackTraceElement = e.getStackTrace()[0];
                System.out.println("系统出错，错误信息:" + e.toString() + " at " + stackTraceElement.getClassName() + "."
                        + stackTraceElement.getMethodName() + ":" + stackTraceElement.getLineNumber());
                throw new ServiceException(e.getMessage());
            }
        });

        //批量插入
        //this.saveBatch(rmComparisons);
        return rmComparisons;
    }

    @Override
    public boolean updateBySsccList(List<String> ssccList) {
        //根据ssccList更新status为已调整
        LambdaUpdateWrapper<RmComparison> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.in(RmComparison::getSsccNumber, ssccList);
        lambdaUpdateWrapper.eq(RmComparison::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        lambdaUpdateWrapper.eq(RmComparison::getStatus, ComparisonEnum.DIFF.code());
        lambdaUpdateWrapper.set(RmComparison::getStatus, ComparisonEnum.CHANGED.code());
        boolean update = this.update(lambdaUpdateWrapper);
        return update;
    }


    @Override
    public List<ProComparison> insertProComparison(List<ProComparison> proComparisons) {

        try {


            //获取proComparisons中的materialNb和batch的map
            List<Map<String, String>> materialNbAndBatchMapList = proComparisons.stream().map(proComparison -> {
                //判断batch格式是否为”yyyy.mm.dd“字符串格式
                if (StringUtils.isBlank(proComparison.getBatch())) {
                    throw new ServiceException("存在空的Batch");
                }
                if (!proComparison.getBatch().matches("\\d{4}\\.\\d{2}\\.\\d{2}")) {
                    throw new ServiceException("Batch格式不正确：" + proComparison.getBatch());
                }

                String replace = proComparison.getBatch().replace(".", "-");
                Map<String, String> map = new HashMap<>();
                map.put("materialNb", proComparison.getMaterialNb());
                map.put("batch", replace);
                return map;
            }).collect(Collectors.toList());

            //查询prolist
            List<ProductStockVO> productStockVOS = productStockMapper.listByMaterials(materialNbAndBatchMapList);

            //proComparisons循环赋值
            proComparisons.forEach(r -> {
                String replace = r.getBatch().replace(".", "-");
                Date date = new Date();
                //replace转date
                try {
                    date = DateUtils.parseDate(replace, "yyyy-MM-dd");
                } catch (ParseException e) {
                    throw new RuntimeException(r.getBatch() + "格式出错");
                }
                //根据materialNb和batch查询productStockVOs
                Date finalDate = date;
                List<ProductStockVO> productStockVOs = productStockVOS.stream().
                        filter(productStockVO ->
                                productStockVO.getMaterialNb().equals(r.getMaterialNb()) &&
                                        productStockVO.getExpireDate().equals(finalDate)).collect(Collectors.toList());
                //productStockVOs的totalstock累加
//                Double reduce = productStockVOs.stream().map(ProductStockVO::getTotalStock).reduce(0d, Double::sum);
                Double reduce = productStockVOs.stream().map(item -> item.getTotalStock() * Double.valueOf(item.getBoxSpecification())).reduce(0d, Double::sum);

                //箱 Tr 对应包装规格
                //String boxSpecification = productStockVOs.get(0).getBoxSpecification();
                //boxSpecification转double
                //Double boxSpecificationDouble = Double.valueOf(boxSpecification);

                r.setStockQuantity(reduce);
                r.setStatus(ComparisonEnum.DIFF.code());
                //判断库存值是否相等

                BigDecimal total = null;
                try {
                    total = new BigDecimal(NumberFormat.getInstance().parse(r.getUnrestricted()).doubleValue())
                            .add(new BigDecimal(NumberFormat.getInstance().parse(r.getInQualityInsp()).doubleValue()))
                            .add(new BigDecimal(NumberFormat.getInstance().parse(r.getRestrictedUseStock()).doubleValue()))
                            .add(new BigDecimal(NumberFormat.getInstance().parse(r.getBlocked()).doubleValue()))
                            .add(new BigDecimal(NumberFormat.getInstance().parse(r.getStockInTransit()).doubleValue()));
                } catch (ParseException e) {
                    throw new ServiceException("数据格式有误");
                }
                if (CompareUtils.isEqual(total, new BigDecimal(reduce))) {
                    r.setStatus(ComparisonEnum.SAME.code());
                }
            });
        } catch (Exception e) {

            throw new ServiceException(e.getMessage());
        }
        boolean b = proComparisonService.saveBatch(proComparisons);
        if (b) {
            return proComparisons;
        }
        return null;
    }

    @Override
    public boolean deleteRmComparisonByCreat() {
        String username = SecurityUtils.getUsername();
        QueryWrapper<RmComparison> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("create_by", username);
        int delete = rmComparisonMapper.delete(queryWrapper);
        return delete >= 0;
    }

}




