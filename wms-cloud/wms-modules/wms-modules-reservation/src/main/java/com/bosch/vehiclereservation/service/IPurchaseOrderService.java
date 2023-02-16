package com.bosch.vehiclereservation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.vehiclereservation.api.domain.PurchaseOrder;

import java.util.List;

public interface IPurchaseOrderService extends IService<PurchaseOrder> {

    /**
     * 查询采购单列表
     *
     * @param purchaseOrder 采购单
     * @return 采购单集合
     */
    public List<PurchaseOrder> selectPurchaseOrderList(PurchaseOrder purchaseOrder);

    /**
     * 查询供应商可预约的采购单列表
     *
     * @param name          供应商名称
     * @param purchaseOrder 查询信息
     * @return 采购单集合
     */
    public List<PurchaseOrder> selectSupplierPurchaseOrder(String name, PurchaseOrder purchaseOrder);

}
