package com.bosch.product.api.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 
 * @TableName shipping_task
 */
@Data
@ApiModel(value = "ShippingTaskDTO", description = "打包任务")
public class ShippingTaskVO extends BaseEntity {

    @ApiModelProperty(value = "id")
    private  Long id;

    @ApiModelProperty(value = "打包计划id")
    private  String shippingPlanId;

    @ApiModelProperty(value = "打包批次号")
    private String packageNo;

    @ApiModelProperty(value = "Shipping Mark")
    private String shippingMark;

    @ApiModelProperty(value = "ETO PO")
    private String etoPo;

    @ApiModelProperty(value = "ETO PLANT")
    private String etoPlant;

    @ApiModelProperty(value = "stock movement 移库日期")
    private String stockMovementDate;

    @ApiModelProperty(value = "stock movement 移库日期排序字段")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date stockMovementDateOrderBy;

    @ApiModelProperty(value = "Country")
    private String country;

    @ApiModelProperty(value = "Prod-order")
    private String prodOrder;

    @ApiModelProperty(value = "Qty")
    private Integer qty;

    @ApiModelProperty(value = "是否拆托")
    private String isDisassembled;

    @ApiModelProperty(value = "TR")
    private String tr;

    @ApiModelProperty(value = "SAP Code")
    private String sapCode;

    @ApiModelProperty(value = "Pallet Quantity")
    private String palletQuantity;

    @ApiModelProperty(value = "after packing")
    private String afterPacking;

    /**
     * 状态（1：已执行，0：未执行）
     */
    @ApiModelProperty(value = "状态（2：已执行，1:执行中,0：未执行）")
    private Integer status;

    @ApiModelProperty(value = "进度")
    private Double rateOfProgress;
}