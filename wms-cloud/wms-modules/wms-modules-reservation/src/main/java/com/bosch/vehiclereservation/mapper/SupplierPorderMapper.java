package com.bosch.vehiclereservation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.vehiclereservation.api.domain.SupplierPorder;
import com.bosch.vehiclereservation.api.domain.vo.SupplierReserveDetailVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Mapper
@Repository("supplierPorderMapper")
public interface SupplierPorderMapper extends BaseMapper<SupplierPorder> {

    public BigDecimal getArriveQuantityByPurchaseId(Long purchaseId);

    public Integer updateSurplusQuantityByPurchaseId(@Param("purchaseId") Long purchaseId, @Param("surplusQuantity") BigDecimal surplusQuantity, @Param("arriveQuantity") BigDecimal arriveQuantity);

    public List<SupplierReserveDetailVO> getSupplierReserveList(@Param("purchaseId") Long purchaseId);
}
