package com.bosch.vehiclereservation.api.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PurchaseOrderVO {

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    private Long purchaseId;

    /**
     * 订单PO号
     */
    @ApiModelProperty(value = "订单PO号")
    private String poCode;

    /**
     * 订单行号
     */
    @ApiModelProperty(value = "订单行号")
    private String poItem;

    /**
     * 供应商名称
     */
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    /**
     * 料号
     */
    @ApiModelProperty(value = "料号")
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(value = "物料名称")
    private String materialName;

    /**
     * 需求数量
     */
    @ApiModelProperty(value = "需求数量")
    private BigDecimal quantity;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位")
    private String unit;

    /**
     * 客户预计供应商到货日期
     */
    @ApiModelProperty(value = "客户预计供应商到货日期")
    private Date deliveryDate;

    /**
     * 需求放行日期
     */
    @ApiModelProperty(value = "需求放行日期")
    private Date releaseDate;

    /**
     * 首批变更号
     */
    @ApiModelProperty(value = "首批变更号")
    private String firstBatchChangeNo;

    /**
     * 海关台账号
     */
    @ApiModelProperty(value = "海关台账号")
    private String cmsNumber;

    /**
     * 状态 0：正常 1：关闭
     */
    @ApiModelProperty(value = "状态 0：正常 1：关闭")
    private Integer status;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

}
