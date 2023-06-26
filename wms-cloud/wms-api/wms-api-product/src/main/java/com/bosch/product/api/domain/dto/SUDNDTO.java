package com.bosch.product.api.domain.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.common.core.web.page.PageDomain;
import lombok.Data;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-06-26 15:33
 **/
@Data
public class SUDNDTO extends PageDomain {
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

    @ExcelProperty(value = "Delivery quantity")
    private String deliveryQuantity;

    @ExcelProperty(value = "Sales Unit")
    private String salesUnit;



}
