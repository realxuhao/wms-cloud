package com.bosch.product.api.domain.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.common.core.web.page.PageDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-06-26 15:33
 **/
@Data
public class SUDNDTO extends PageDomain {

    private Long id;

    @ExcelProperty(value = "Delivery")
    private String delivery;

    @ExcelProperty(value = "Plant")
    private String plant;

    @ExcelProperty(value = "Ship-To Party")
    private String shipToParty;

    @ExcelProperty(value = "Name of the ship-to party")
    private String nameOfShipToParty;

    @ExcelProperty(value = "Deliv. date(From/to)")
    private String deliveryDate;

    @ExcelProperty(value = "Item")
    private String item;

    @ExcelProperty(value = "Material")
    private String material;


    private Double deliveryQuantity;

    @ExcelProperty(value = "Delivery quantity")
    private String deliveryQuantityString;

    @ExcelProperty(value = "Sales Unit")
    private String salesUnit;

    @ExcelProperty(value = "Batch")
    private String batch;

    @ExcelProperty(value = "Storage Location")
    private String storageLocation;

    @ApiModelProperty(value = "0:待生成，1：已生成")
    private Integer status;


    @ApiModelProperty(value = "0:未完成，1：全部完成")
    private Integer pickStatus;


    private Double newQuantity;

    private Integer type;//0:未完成，1：已完成


    @ApiModelProperty(value = "开始创建时间")
    private Date startCreateTime;
    @ApiModelProperty(value = "结束创建时间")
    private Date endCreateTime;



}
