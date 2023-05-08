package com.bosch.product.api.domain.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.web.domain.BaseEntity;
import com.ruoyi.common.core.web.page.PageDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 原材料库存对比
 * @TableName rm_comparison
 */
@Data
public class RmComparisonDTO extends PageDomain {
    /**
     * plant_nb
     */
    @ApiModelProperty(value = "plantNb")
    @ExcelProperty("PlantNb")
    private String plantNb;

    /**
     * sscc_number
     */
    @ApiModelProperty(value = "ssccNumber")
    @ExcelProperty("SSCCNumber")
    private String ssccNumber;

    /**
     * sap_material_code
     */
    @ApiModelProperty(value = "sapMaterialCode")
    @ExcelProperty("SAPMaterialCode")
    private String sapMaterialCode;

    /**
     * sap_batch_number
     */
    @ApiModelProperty(value = "sapBatchNumber")
    @ExcelProperty("SAPBatchNumber")
    private String sapBatchNumber;

    /**
     * remaining_qty
     */
    @ApiModelProperty(value = "remainingQty")
    @ExcelProperty("RemainingQty")
    private String remainingQty;

    /**
     * unit_of_measure
     */
    @ApiModelProperty(value = "unitOfMeasure")
    @ExcelProperty("UnitOfMeasure")
    private String unitOfMeasure;

    /**
     * r3_stock_status
     */
    @ApiModelProperty(value = "r3StockStatus")
    @ExcelProperty("R3StockStatus")
    private String r3StockStatus;


    /**
     * 状态（1：已执行，0：未执行）
     */
    @ApiModelProperty(value = "status")
    private Integer status;

    /**
     * 删除标记1：删除，0:可用
     */
    @ApiModelProperty(value = "deleteFlag")
    private Integer deleteFlag;




}