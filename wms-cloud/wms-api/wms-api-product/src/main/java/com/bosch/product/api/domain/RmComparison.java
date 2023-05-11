package com.bosch.product.api.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.ruoyi.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 原材料库存对比
 * @TableName rm_comparison
 */
@TableName(value ="rm_comparison")
@Data
public class RmComparison extends BaseEntity {
    /**
     * plant_nb
     */
    @TableField(value = "plant_nb")
    private String plantNb;

    /**
     * sscc_number
     */
    @TableField(value = "sscc_number")
    private String ssccNumber;

    /**
     * sap_material_code
     */
    @TableField(value = "sap_material_code")
    private String sapMaterialCode;

    /**
     * sap_batch_number
     */
    @TableField(value = "sap_batch_number")
    private String sapBatchNumber;

    /**
     * remaining_qty
     */
    @TableField(value = "remaining_qty")
    private String remainingQty;

    /**
     * unit_of_measure
     */
    @TableField(value = "unit_of_measure")
    private String unitOfMeasure;

    /**
     * r3_stock_status
     */
    @TableField(value = "r3_stock_status")
    private String r3StockStatus;

    /**
     * StockSapMaterialCode
     */
    @TableField(value = "stock_sap_material_code")
    private String stockSapMaterialCode;


    /**
     * StockSapBatchNumber
     */
    @TableField(value = "stock_sap_batch_number")
    private String stockSapBatchNumber;

    /**
     * StockRemainingQty
     */
    @TableField(value = "stock_remaining_qty")
    private String stockRemainingQty;

    /**
     * StockUnitOfMeasure
     */
    @TableField(value = "stock_unit_of_measure")
    private String stockUnitOfMeasure;

    /**
     * StockR3StockStatus
     */
    @TableField(value = "stock_r3_stock_status")
    private String stockR3StockStatus;
    /**
     * 状态（1：已执行，0：未执行）
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 删除标记1：删除，0:可用
     */
    @TableField(value = "delete_flag")
    private Integer deleteFlag;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}