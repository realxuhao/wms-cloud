package com.bosch.product.api.domain.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
@ApiModel(value = "ShippingPlanDTO", description = "Shipping Plan DTO")
public class ShippingPlanVO {

    @ApiModelProperty(value = "Shipping Mark")
    private String shippingMark;

    @ApiModelProperty(value = "ETO PO")
    private String etoPo;

    @ApiModelProperty(value = "ETO PLANT")
    private String etoPlant;

    @ApiModelProperty(value = "stock movement 移库日期字符类型")
    private String stockMovementDate;

    @ApiModelProperty(value = "stock movement 移库日期时间类型")
    private Date stockMovementDateConvert;

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

}

