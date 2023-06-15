package com.bosch.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.product.api.domain.ProductStock;
import com.bosch.product.api.domain.SPDN;
import com.bosch.product.api.domain.ShippingTask;
import com.bosch.product.api.domain.dto.SPDNDTO;
import com.bosch.product.api.domain.vo.SPDNVO;
import com.bosch.product.mapper.SPDNMapper;
import com.bosch.product.mapper.ShippingTaskMapper;
import com.bosch.product.service.IProductStockService;
import com.bosch.product.service.ISPDNService;
import com.bosch.product.service.IShippingTaskService;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.enums.QualityStatusEnums;
import com.ruoyi.common.core.exception.ServiceException;
import org.hibernate.validator.internal.util.privilegedactions.NewSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-06-12 12:55
 **/
@Service
public class SPDNServiceImpl extends ServiceImpl<SPDNMapper, SPDN>
        implements ISPDNService {


    @Autowired
    private SPDNMapper spdnMapper;

    @Autowired
    private IProductStockService productStockService;


    @Override
    public List<SPDNVO> getList(SPDNDTO spdndto) {
        return spdnMapper.getList(spdndto);
    }

    @Override
    public void batchDelete(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("必须选中一条数据才能操作");
        }
        LambdaUpdateWrapper<SPDN> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(SPDN::getId, ids);
        updateWrapper.set(SPDN::getDeleteFlag, DeleteFlagStatus.TRUE.getCode());
        this.update(updateWrapper);
    }


    @Override
    public void approve(List<Long> idsList) {
        //审批需要校验1、库存是不是在7752。2、数量是否符合
        Assert.notEmpty(idsList, "需选中数据后再进行审批");
        List<SPDN> spdnList = this.listByIds(idsList);
        Map<String, Double> ssccQtyMap = spdnList.stream().collect(Collectors.toMap(SPDN::getSsccNumber, SPDN::getQty));
        Map<String, String> ssccPlantMap = spdnList.stream().collect(Collectors.toMap(SPDN::getSsccNumber, SPDN::getPlant));
        List<String> ssccList = spdnList.stream().map(SPDN::getSsccNumber).collect(Collectors.toList());
        LambdaQueryWrapper<ProductStock> stockLambdaQueryWrapper = new LambdaQueryWrapper<>();
        stockLambdaQueryWrapper.in(ProductStock::getSsccNumber, ssccList);
        stockLambdaQueryWrapper.eq(ProductStock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        List<ProductStock> stockList = productStockService.list(stockLambdaQueryWrapper);
        List<String> inValidPlantSSCCList = new ArrayList<>();
        List<String> inValidQtySSCCList = new ArrayList<>();
        stockList.stream().forEach(item -> {
            if (!item.getPlantNb().equals("7752")) {
                inValidPlantSSCCList.add(item.getSsccNumber());
            }
            if (item.getTotalStock() != ssccQtyMap.get(item.getSsccNumber())) {
                inValidQtySSCCList.add(item.getSsccNumber());
            }
            if (!item.getQualityStatus().equals(QualityStatusEnums.USE.getCode())) {
                throw new ServiceException(item.getSsccNumber() + "库存状态不为U的库存数据");
            }
        });

        if (!CollectionUtils.isEmpty(inValidPlantSSCCList) || !CollectionUtils.isEmpty(inValidQtySSCCList)) {
            throw new ServiceException("存在" + inValidPlantSSCCList.size() + "条库存不在7752的数据\n" + "存在" + inValidQtySSCCList.size() + "条数量不正确的数据");
        }


        //7761的产品同时对应的SSCC质量状态由U变为Q，对应的SSCC流转到销售库存页面，同时生成SUQ质检管理页面（原有的成品库存不再存在）。
        //7701不生成SUQA质检，对应的SSCC生成移库任务（下架，装车（track））

        // 如果是7761。那么就把库存的plantNb改成7761,同时质量状态变为Q
        stockList.stream().forEach(stock->{
//            if ()
        });



    }


}
