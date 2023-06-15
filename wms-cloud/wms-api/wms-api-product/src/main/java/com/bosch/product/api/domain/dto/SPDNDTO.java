package com.bosch.product.api.domain.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.page.PageDomain;
import lombok.Data;

import java.text.DecimalFormat;

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



    @ExcelProperty(value = "ProdBatch")
    private String prodBatch;


    private int status;



    public void setQty(String qty) {
        try {
            this.qty=new DecimalFormat().parse(this.qtyString).doubleValue();

        }catch(Exception e){
            throw new ServiceException("Qty列格式不正确");
        }
    }



}
