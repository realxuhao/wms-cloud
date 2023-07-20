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
    @Excel(name = "Delivery")
    private String delivery;
    @Excel(name = "Plant")
    private String plant;
    @Excel(name = "Ship-To Party")
    private String shipToParty;
    @Excel(name = "Name of the ship-to party")
    private String nameOfShipToParty;
    @Excel(name = "Deliv.date(From/to)")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String deliveryDate;
    @Excel(name = "Item")
    private String item;
    @Excel(name = "Material")
    private String material;
    @Excel(name = "Batch")
    private String batch;
    @Excel(name = "Production batch",dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date productionBatch;
    @Excel(name = "Delivery Quantity")
    private Double deliveryQuantity;
    @Excel(name = "Sales Unit")
    private String salesUnit;
}
