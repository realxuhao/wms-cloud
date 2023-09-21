package com.bosch.product.api.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-07-07 09:27
 **/
@Data
public class ProductPickExportVO {

    @Excel(name = "Name of the ship-to party")
    private String nameOfShipToParty;

    @Excel(name = "External Ref")
    private String externalRef;
    @Excel(name = "Plant")
    private String plant;

    @Excel(name = "DeliveryNb")
    private String delivery;
    @Excel(name = "Ship-To")
    private String shipToParty;

    @Excel(name = "Ship Date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String deliveryDate;

    @Excel(name = "Vendor Code")
    private String vendorCode;


    @Excel(name = "SSCCNumber")
    private String ssccNumber;

    @Excel(name = "Delivery_Item")
    private String item;
    @Excel(name = "Material")
    private String material;
    @Excel(name = "Batch", dateFormat = "yyyy.MM.dd")
    @JsonFormat(pattern = "yyyy.MM.dd")
    private Date batch;
    @Excel(name = "Qty")
    private String deliveryQuantityString;
    @Excel(name = "Uom")
    private String salesUnit;
    @Excel(name = "StorageLocation",defaultValue = "0001")
    private String storageLocation;
    @Excel(name = "ProdBatch")
    private String productionBatch;
    //    @Excel(name = "Delivery Quantity")
    private Double deliveryQuantity;


}
