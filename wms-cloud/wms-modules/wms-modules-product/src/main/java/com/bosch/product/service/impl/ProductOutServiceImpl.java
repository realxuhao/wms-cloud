package com.bosch.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bosch.binin.api.enumeration.SPDNStatusEnum;
import com.bosch.product.api.domain.ProductStock;
import com.bosch.product.api.domain.dto.SPDNDTO;
import com.bosch.product.service.IProductOutService;
import com.bosch.product.service.IProductStockService;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.enums.QualityStatusEnums;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-06-12 10:10
 **/
@Service
public class ProductOutServiceImpl implements IProductOutService {


    @Autowired
    private IProductStockService productStockService;

    @Override
    public void validList(List<SPDNDTO> dtos) {
        //校验质量状态
        List<String> ssccList = dtos.stream().map(SPDNDTO::getSsccNumber).collect(Collectors.toList());
        LambdaQueryWrapper<ProductStock> stockWrapper = new LambdaQueryWrapper<>();
        stockWrapper.in(ProductStock::getSsccNumber, ssccList);
        stockWrapper.in(ProductStock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        List<ProductStock> productStocks = productStockService.list(stockWrapper);
        List<String> notUseSsccList = productStocks.stream().filter(item -> !item.getQualityStatus().equals(QualityStatusEnums.USE.getCode())).map(ProductStock::getSsccNumber).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(notUseSsccList)) {
            throw new ServiceException("存在以下状态不为U的库存" + notUseSsccList);
        }

        dtos.stream().forEach(item -> {
            setQty(item.getQtyString(), item);
            item.setStatus(SPDNStatusEnum.WAITING_APPROVE.code());
        });
    }

    public void setQty(String qty, SPDNDTO spdndto) {
        try {
            if (StringUtils.isNotEmpty(qty)) {
                spdndto.setQty(new DecimalFormat().parse(qty).doubleValue());
            }

        } catch (Exception e) {
            throw new ServiceException("Qty列格式不正确");
        }
    }


}
