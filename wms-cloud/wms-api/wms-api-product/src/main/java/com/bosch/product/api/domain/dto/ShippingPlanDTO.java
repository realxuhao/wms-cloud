package com.bosch.product.api.domain.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.web.domain.BaseEntity;
import com.ruoyi.common.core.web.page.PageDomain;
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
public class ShippingPlanDTO extends PageDomain {

    @ApiModelProperty(value = "Shipping Mark")
    @ExcelProperty("Shipping  Mark")
    private String shippingMark;

    @ApiModelProperty(value = "ETO PO")
    @ExcelProperty("ETO PO")
    private String etoPo;

    @ApiModelProperty(value = "ETO PLANT")
    @ExcelProperty("ETO PLANT")
    private String etoPlant;

    @ApiModelProperty(value = "stock movement 移库日期")
    @ExcelProperty("stock movement 移库日期")
    private String stockMovementDate;


    @ApiModelProperty(value = "Country")
    @ExcelProperty("Country")
    private String country;

    @ApiModelProperty(value = "Prod-order")
    @ExcelProperty("Prod-order")
    private String prodOrder;

    @ApiModelProperty(value = "Qty")
    @ExcelProperty("Qty")
    private Integer qty;

    @ApiModelProperty(value = "是否拆托")
    @ExcelProperty("是否拆托")
    private String isDisassembled;

    @ApiModelProperty(value = "TR")
    @ExcelProperty("TR")
    private String tr;

    @ApiModelProperty(value = "SAP Code")
    @ExcelProperty("SAP Code")
    private String sapCode;

    @ApiModelProperty(value = "Pallet Quantity")
    @ExcelProperty("Pallet Quantity")
    private String palletQuantity;

    @ApiModelProperty(value = "after packing")
    @ExcelProperty("after packing")
    private String afterPacking;

}

