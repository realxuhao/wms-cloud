package com.bosch.product.api.domain.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 成品对比表
 * @TableName pro_comparison
 */
@TableName(value ="pro_comparison")
@Data
public class ProComparisonDTO extends BaseEntity {
    /**
     * 
     */
    @ExcelIgnore
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * plant_nb
     */
    @ExcelProperty(value = "Plant")
    @ApiModelProperty(value = "plantNb")
    private String plantNb;

    /**
     * stock_plant_nb
     */
    @ExcelIgnore
    @ApiModelProperty(value = "stockPlantNb")
    private String stockPlantNb;

    /**
     * 
     */
    @ExcelProperty(value = "Material Description")
    @ApiModelProperty(value = "materialDescription")
    private String materialDescription;

    /**
     * Material
     */
    @ExcelProperty(value = "Material")
    @ApiModelProperty(value = "materialNb")
    private String materialNb;

    /**
     * stock_material_nb
     */
    @ExcelIgnore
    @ApiModelProperty(value = "stockMaterialNb")
    private String stockMaterialNb;

    /**
     * Unrestricted
     */
    @ExcelProperty(value = "Unrestricted")
    @ApiModelProperty(value = "unrestricted")
    private String unrestricted;

    /**
     * Unit
     */
    @ExcelProperty(value = "Base Unit of Measure")
    @ApiModelProperty(value = "unit")
    private String unit;

    /**
     * Storage Location
     */
    @ExcelProperty(value = "Storage Location")
    @ApiModelProperty(value = "storageLocation")
    private String storageLocation;

    /**
     * Batch
     */
    @ExcelProperty(value = "Batch")
    @ApiModelProperty(value = "batch")
    private String batch;

    /**
     * 
     */
    @ExcelIgnore
    @ApiModelProperty(value = "stockExpireDate")
    private String stockExpireDate;

    /**
     * In Quality Insp.
     */
    @ExcelProperty(value = "In Quality Insp.")
    @ApiModelProperty(value = "inQualityInsp")
    private String inQualityInsp;

    /**
     * Restricted-Use Stock
     */
    @ExcelProperty(value = "Restricted-Use Stock")
    @ApiModelProperty(value = "restrictedUseStock")
    private String restrictedUseStock;

    /**
     * 
     */
    @ExcelIgnore
    @ApiModelProperty(value = "stockQuantity")
    private Double stockQuantity;

    /**
     * Blocked
     */
    @ExcelProperty(value = "Blocked")
    @ApiModelProperty(value = "blocked")
    private String blocked;

    /**
     * Returns
     */
    @ExcelProperty(value = "Returns")
    @ApiModelProperty(value = "returns")
    private String returns;

    /**
     * Stock in Transit
     */
    @ExcelProperty(value = "Stock in Transit")
    @ApiModelProperty(value = "stockInTransit")
    private String stockInTransit;

    /**
     * In transfer (plant)
     */
    @ExcelProperty(value = "In transfer (plant)")
    @ApiModelProperty(value = "inTransfer")
    private String inTransfer;


    /**
     * 状态（1：相同，0：不同）
     */
    @ApiModelProperty(value = "status")
    @ExcelIgnore
    private Integer status;

    /**
     * 删除标记1：删除，0:可用
     */
    @ApiModelProperty(value = "deleteFlag")
    @ExcelIgnore
    private Integer deleteFlag;

    /**
     * cell
     */
    @ExcelProperty(value = "Cell")
    @ApiModelProperty(value = "cell")
    private String cell;

}