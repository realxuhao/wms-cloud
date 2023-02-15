package com.bosch.vehiclereservation.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.vehiclereservation.api.domain.PurchaseOrder;
import com.bosch.vehiclereservation.mapper.PurchaseOrderMapper;
import com.bosch.vehiclereservation.service.IPurchaseOrderService;
import com.bosch.vehiclereservation.service.ISupplierPorderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PurchaseOrderServiceImpl extends ServiceImpl<PurchaseOrderMapper, PurchaseOrder> implements IPurchaseOrderService {

    @Autowired
    private PurchaseOrderMapper purchaseOrderMapper;

    @Autowired
    private ISupplierPorderService supplierPorderService;

    @Override
    public List<PurchaseOrder> selectPurchaseOrderList(PurchaseOrder purchaseOrder) {
        return purchaseOrderMapper.selectPurchaseOrderList(purchaseOrder);
    }

    @Override
    public List<PurchaseOrder> selectPurchaseOrderByName(String name) {
        List<PurchaseOrder> purchaseOrders = purchaseOrderMapper.selectPurchaseOrderByName(name);
        purchaseOrders.forEach(c -> {
            BigDecimal sum = supplierPorderService.getArriveQuantityByPurchaseId(c.getPurchaseId());
            BigDecimal subtract = c.getQuantity().subtract(sum);
            c.setQuantity(subtract);
        });
        return purchaseOrders;
    }
}
