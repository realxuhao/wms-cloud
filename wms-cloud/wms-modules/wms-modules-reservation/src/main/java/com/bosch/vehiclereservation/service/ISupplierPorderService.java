package com.bosch.vehiclereservation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.vehiclereservation.api.domain.SupplierPorder;
import io.swagger.models.auth.In;

import java.math.BigDecimal;

public interface ISupplierPorderService extends IService<SupplierPorder> {

    public BigDecimal getArriveQuantityByPurchaseId(Long purchaseId);

}
