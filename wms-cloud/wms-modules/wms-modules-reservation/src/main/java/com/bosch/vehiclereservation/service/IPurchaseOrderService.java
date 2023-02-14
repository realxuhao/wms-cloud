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
     * 根据供应商名称查询列表
     *
     * @param name 供应商名称
     * @return 采购单集合
     */
    public List<PurchaseOrder> selectPurchaseOrderByName(String name);

}
