package com.bosch.vehiclereservation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.vehiclereservation.api.domain.PurchaseOrder;
import com.bosch.vehiclereservation.api.domain.vo.SupplierReserveDetailVO;

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

    /**
     * 关闭采购单
     *
     * @param id 采购单id
     * @return
     */
    public boolean closePurchaseOrder(Long id);

    /**
     * 获取某个采购单的供应商预约信息列表
     *
     * @param purchaseId 采购单id
     * @return
     */
    public List<SupplierReserveDetailVO> getSupplierReserveList(Long purchaseId);

    /**
     * 获取错误状态的采购单PO号
     *
     * @return
     */
    public List<String> getAllErrorPoCode();

    public List<String> getPoCodeList(String name);

    public List<String> getPoItemList(String name);

    public List<String> getCmsNumberList(String name);
}
