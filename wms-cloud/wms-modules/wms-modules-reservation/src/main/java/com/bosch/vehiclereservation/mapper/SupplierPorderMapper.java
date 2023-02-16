package com.bosch.vehiclereservation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.vehiclereservation.api.domain.SupplierPorder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Mapper
@Repository("supplierPorderMapper")
public interface SupplierPorderMapper extends BaseMapper<SupplierPorder> {

    public BigDecimal getArriveQuantityByPurchaseId(Long purchaseId);

    public Integer updateSurplusQuantityByPurchaseId(@Param("purchaseId") Long purchaseId, @Param("surplusQuantity") BigDecimal surplusQuantity, @Param("arriveQuantity") BigDecimal arriveQuantity);

}
