package com.bosch.binin.api.domain.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-05-25 10:43
 **/

@Data
public class InitStockDTO {

    @ExcelProperty(value = "PlantNb")
    private String plantNb;

    @ExcelProperty(value = "SSCCNumber")
    private String ssccNumber;

    @ExcelProperty(value = "SAPMaterialCode")
    private String sapMaterialCode;

    @ExcelProperty(value = "SAPBatchNumber")
    private String sapBatchNumber;

    @ExcelProperty(value = "DefaultQty")
    private String defaultQty;

    @ExcelProperty(value = "RemainingQty")
    private String remainingQty;

    @ExcelProperty(value = "Dluo")
    private String dluo;

    @ExcelProperty(value = "SAPStorageLocation")
    private String sapStorageLocation;

    @ExcelProperty(value = "Area")
    private String area;

    @ExcelProperty(value = "Bin")
    private String bin;

    @ExcelProperty(value = "PONumber")
    private String poNumber;

    @ExcelProperty(value = "R3StockStatus")
    private String r3StockStatus;
}
