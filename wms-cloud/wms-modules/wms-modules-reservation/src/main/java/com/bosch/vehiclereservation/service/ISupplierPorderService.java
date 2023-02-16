package com.bosch.vehiclereservation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.vehiclereservation.api.domain.SupplierPorder;

import java.math.BigDecimal;

public interface ISupplierPorderService extends IService<SupplierPorder> {

    public BigDecimal getArriveQuantityByPurchaseId(Long purchaseId);

    public Integer updateSurplusQuantityByPurchaseId(Long purchaseId, BigDecimal surplusQuantity, BigDecimal arriveQuantity);


}
