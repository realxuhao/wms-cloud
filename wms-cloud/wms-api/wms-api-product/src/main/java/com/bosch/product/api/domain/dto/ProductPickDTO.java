package com.bosch.product.api.domain.dto;

import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-06-27 11:06
 **/
@Data
public class ProductPickDTO {

    private String delivery;
    private String plant;
    private String shipToParty;
    private String nameOfShipToParty;
    private String deliveryDate;
    private String item;
    private String material;
    private String sscc;
    private String batch;
    private String productionBatch;
    private Double deliveryQuantity;
    private String salesUnit;
    private Integer status;
    private String licensePlateNumber;
    private Date binDownDate;
    private String binDownPerson;
    private String shipper;
    private Date shippingTime;
    private Long sudnId;
    @ApiModelProperty(value = "开始创建时间")
    @DateTimeFormat("yyyy-MM-dd mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd mm:ss",timezone = "GMT+8")
    private Date startCreateTime;
    @ApiModelProperty(value = "结束创建时间")
    @DateTimeFormat("yyyy-MM-dd mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd mm:ss",timezone = "GMT+8")
    private Date endCreateTime;

    private String wareCode;
    private String ssccNb;
}
