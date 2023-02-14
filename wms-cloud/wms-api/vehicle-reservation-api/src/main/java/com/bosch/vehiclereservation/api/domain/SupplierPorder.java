package com.bosch.vehiclereservation.api.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.web.domain.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 供应商预约单关联的采购信息 vr_supplier_porder
 *
 * @author taojd
 * @date 2023-02-14
 */
@Data
@TableName("vr_supplier_porder")
public class SupplierPorder extends BaseEntity {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 采购表id
     */
    private Long purchaseId;

    /**
     * 供应商预约单no
     */
    private String reserveNo;

    /**
     * 实际送货数量
     */
    private BigDecimal arriveQuantity;

    /**
     * 剩余送货数量
     */
    private BigDecimal surplusQuantity;

}
