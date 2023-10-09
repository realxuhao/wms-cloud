package com.bosch.product.api.domain.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.page.PageDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.text.DecimalFormat;
import java.util.Date;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-06-09 13:58
 **/
@Data
public class SPDNDTO  extends PageDomain {
    @ExcelProperty(value = "External Ref")
    private String externalRef;

    @ExcelProperty(value = "Plant")
    private String plant;

    @ExcelProperty(value = "DeliveryNb")
    private String deliveryNb;

    @ExcelProperty(value = "Ship_To")
    private String shipTo;

    @ExcelProperty(value = "Ship_Date")
    private String shipDate;

    @ExcelProperty(value = "Vendor Code")
    private String vendorCode;

    @ExcelProperty(value = "SSCCNumber")
    private String ssccNumber;

    @ExcelProperty(value = "Delivery_Item")
    private String deliveryItem;

    @ExcelProperty(value = "Material")
    private String material;

    @ExcelProperty(value = "Batch")
    private String batch;

    private Double qty;

    @ExcelProperty(value = "Qty")
    private String qtyString;

    @ExcelProperty(value = "UoM")
    private String uom;

    @ExcelProperty(value = "StorageLocation")
    private String storageLocation;

    @ApiModelProperty(value = "开始创建时间")
    @DateTimeFormat("yyyy-MM-dd mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd mm:ss",timezone = "GMT+8")
    private Date startCreateTime;
    @ApiModelProperty(value = "结束创建时间")
    @DateTimeFormat("yyyy-MM-dd mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd mm:ss",timezone = "GMT+8")
    private Date endCreateTime;



    @ExcelProperty(value = "ProdBatch")
    private String prodBatch;


    private Integer status;

    private String wareCode;

    private String areaCode;

    @DateTimeFormat("yyyy-MM-dd mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd mm:ss",timezone = "GMT+8")
    private Date shipDateEnd;
    @DateTimeFormat("yyyy-MM-dd mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd mm:ss",timezone = "GMT+8")
    private Date shipDateStart;


    private String cell;







}
