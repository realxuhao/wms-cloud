package com.bosch.vehiclereservation.api.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SupplierPorderVO {

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    private Long id;

    /**
     * 采购表id
     */
    @ApiModelProperty(value = "采购表id")
    private Long purchaseId;

    /**
     * 供应商预约单no
     */
    @ApiModelProperty(value = "供应商预约单no")
    private String reserveNo;

    /**
     * 实际送货数量
     */
    @ApiModelProperty(value = "实际送货数量")
    private BigDecimal arriveQuantity;

    /**
     * 剩余送货数量
     */
    @ApiModelProperty(value = "剩余送货数量")
    private BigDecimal surplusQuantity;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

}
