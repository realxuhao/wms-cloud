package com.bosch.vehiclereservation.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.masterdata.api.RemoteMasterDataService;
import com.bosch.masterdata.api.domain.vo.SupplierInfoVO;
import com.bosch.vehiclereservation.api.domain.PurchaseOrder;
import com.bosch.vehiclereservation.api.domain.vo.SupplierReserveDetailVO;
import com.bosch.vehiclereservation.api.enumeration.OrderStatusEnum;
import com.bosch.vehiclereservation.mapper.PurchaseOrderMapper;
import com.bosch.vehiclereservation.mapper.SupplierPorderMapper;
import com.bosch.vehiclereservation.service.IPurchaseOrderService;
import com.bosch.vehiclereservation.service.ISupplierPorderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PurchaseOrderServiceImpl extends ServiceImpl<PurchaseOrderMapper, PurchaseOrder> implements IPurchaseOrderService {

    @Autowired
    private PurchaseOrderMapper purchaseOrderMapper;

    @Autowired
    SupplierPorderMapper supplierPorderMapper;

    @Autowired
    private ISupplierPorderService supplierPorderService;

    @Autowired
    private RemoteMasterDataService remoteMasterDataService;

    @Override
    public List<PurchaseOrder> selectPurchaseOrderList(PurchaseOrder purchaseOrder) {
        return purchaseOrderMapper.selectPurchaseOrderList(purchaseOrder);
    }

    @Override
    public List<PurchaseOrder> selectSupplierPurchaseOrder(String name, PurchaseOrder purchaseOrder) {
        if(!"errorname".equals(name)){
            purchaseOrder.setSupplier(name);
        }
        if("admin".equals(purchaseOrder.getSupplier())){
            purchaseOrder.setSupplier("");
        }
        List<PurchaseOrder> purchaseOrders = purchaseOrderMapper.selectSupplierPurchaseOrder(purchaseOrder);
        purchaseOrders.forEach(c -> {
            BigDecimal sum = supplierPorderService.getArriveQuantityByPurchaseId(c.getPurchaseId());
            BigDecimal subtract = c.getQuantity().subtract(sum);
            c.setQuantity(subtract);
        });
        return purchaseOrders;
    }

    @Override
    public boolean closePurchaseOrder(Long id) {
        PurchaseOrder purchaseOrder = purchaseOrderMapper.selectById(id);
        purchaseOrder.setStatus(OrderStatusEnum.CLOSE.getCode());
        int i = purchaseOrderMapper.updateById(purchaseOrder);
        return i > 0;
    }

    @Override
    public List<SupplierReserveDetailVO> getSupplierReserveList(Long purchaseId) {
        List<SupplierReserveDetailVO> supplierReserveList = supplierPorderMapper.getSupplierReserveList(purchaseId);
        Map<String, String> supplierMap = new HashMap<>();
        for (SupplierReserveDetailVO detailVO : supplierReserveList) {
            if (!supplierMap.keySet().contains(detailVO.getSupplierCode())) {
                SupplierInfoVO supplierInfoVO = remoteMasterDataService.getSupplierInfoByCode(detailVO.getSupplierCode()).getData();
                if (supplierInfoVO != null) {
                    supplierMap.put(detailVO.getSupplierCode(), supplierInfoVO.getName());
                }
            }
            detailVO.setSupplierName(supplierMap.get(detailVO.getSupplierCode()));
        }
        return supplierReserveList;
    }

    @Override
    public List<String> getAllErrorPoCode() {
        return purchaseOrderMapper.selectAllErrorPoCode();
    }

    @Override
    public List<String> getPoCodeList(String name) {
        return purchaseOrderMapper.getPoCodeList("admin".equals(name) ? "" : name);
    }

    @Override
    public List<String> getPoItemList(String name) {
        return purchaseOrderMapper.getPoItemList("admin".equals(name) ? "" : name);
    }

    @Override
    public List<String> getCmsNumberList(String name) {
        return purchaseOrderMapper.getCmsNumberList("admin".equals(name) ? "" : name);
    }

    @Override
    public List<String> getSupplierName() {
        return purchaseOrderMapper.getSupplierName();
    }
}
