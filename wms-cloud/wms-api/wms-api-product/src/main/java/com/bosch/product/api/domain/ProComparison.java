package com.bosch.product.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.ruoyi.common.core.web.domain.BaseEntity;
import lombok.Data;

/**
 * 成品对比表
 * @TableName pro_comparison
 */
@TableName(value ="pro_comparison")
@Data
public class ProComparison extends BaseEntity {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * plant_nb
     */
    @TableField(value = "plant_nb")
    private String plantNb;

    /**
     * stock_plant_nb
     */
    @TableField(value = "stock_plant_nb")
    private String stockPlantNb;

    /**
     * 
     */
    @TableField(value = "material_description")
    private String materialDescription;

    /**
     * Material
     */
    @TableField(value = "material_nb")
    private String materialNb;

    /**
     * stock_material_nb
     */
    @TableField(value = "stock_material_nb")
    private String stockMaterialNb;

    /**
     * Unrestricted
     */
    @TableField(value = "unrestricted")
    private String unrestricted;

    /**
     * Unit
     */
    @TableField(value = "unit")
    private String unit;

    /**
     * Storage Location
     */
    @TableField(value = "storage_location")
    private String storageLocation;

    /**
     * Batch
     */
    @TableField(value = "batch")
    private String batch;

    /**
     * 
     */
    @TableField(value = "stock_expire_date")
    private String stockExpireDate;

    /**
     * In Quality Insp.
     */
    @TableField(value = "in_quality_insp")
    private String inQualityInsp;

    /**
     * Restricted-Use Stock
     */
    @TableField(value = "restricted_use_stock")
    private String restrictedUseStock;

    /**
     * 
     */
    @TableField(value = "stock_quantity")
    private Double stockQuantity;

    /**
     * Blocked
     */
    @TableField(value = "blocked")
    private String blocked;

    /**
     * Returns
     */
    @TableField(value = "returns")
    private String returns;

    /**
     * Stock in Transit
     */
    @TableField(value = "stock_in_transit")
    private String stockInTransit;

    /**
     * In transfer (plant)
     */
    @TableField(value = "in_transfer")
    private String inTransfer;


    /**
     * 状态（1：相同，0：不同）
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 删除标记1：删除，0:可用
     */
    @TableField(value = "delete_flag")
    private Integer deleteFlag;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;


}