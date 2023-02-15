package com.bosch.vehiclereservation.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.vehiclereservation.api.domain.SupplierPorder;
import com.bosch.vehiclereservation.mapper.SupplierPorderMapper;
import com.bosch.vehiclereservation.service.ISupplierPorderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class SupplierPorderServiceImpl extends ServiceImpl<SupplierPorderMapper, SupplierPorder> implements ISupplierPorderService {

    @Autowired
    private SupplierPorderMapper supplierPorderMapper;

    @Override
    public BigDecimal getArriveQuantityByPurchaseId(Long purchaseId) {
        return supplierPorderMapper.getArriveQuantityByPurchaseId(purchaseId);
    }
}
