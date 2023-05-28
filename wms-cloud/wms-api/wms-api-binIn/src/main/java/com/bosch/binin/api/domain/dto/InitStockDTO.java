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
    private String PlantNb;

    @ExcelProperty(value = "SSCCNumber")
    private String SSCCNumber;

    @ExcelProperty(value = "SAPMaterialCode")
    private String SAPMaterialCode;

    @ExcelProperty(value = "SAPBatchNumber")
    private String SAPBatchNumber;

    @ExcelProperty(value = "DefaultQty")
    private String DefaultQty;

    @ExcelProperty(value = "RemainingQty")
    private String RemainingQty;

    @ExcelProperty(value = "Dluo")
    private String Dluo;

    @ExcelProperty(value = "SAPStorageLocation")
    private String SAPStorageLocation;

    @ExcelProperty(value = "Area")
    private String Area;

    @ExcelProperty(value = "Bin")
    private String Bin;

    @ExcelProperty(value = "PONumber")
    private String PONumber;

    @ExcelProperty(value = "R3StockStatus")
    private String R3StockStatus;
}
