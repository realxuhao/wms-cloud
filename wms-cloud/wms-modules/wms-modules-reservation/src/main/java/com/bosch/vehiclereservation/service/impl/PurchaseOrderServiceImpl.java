package com.bosch.vehiclereservation.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.vehiclereservation.api.domain.PurchaseOrder;
import com.bosch.vehiclereservation.mapper.PurchaseOrderMapper;
import com.bosch.vehiclereservation.service.IPurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseOrderServiceImpl extends ServiceImpl<PurchaseOrderMapper, PurchaseOrder> implements IPurchaseOrderService {

    @Autowired
    private PurchaseOrderMapper purchaseOrderMapper;

    @Override
    public List<PurchaseOrder> selectPurchaseOrderList(PurchaseOrder purchaseOrder) {
        return purchaseOrderMapper.selectPurchaseOrderList(purchaseOrder);
    }
}
