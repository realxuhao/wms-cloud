package com.bosch.product.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.web.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

@Data
@TableName("shipping_plan")
public class ShippingPlan extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("shipping_mark")
    private String shippingMark;

    @TableField("eto_po")
    private String etoPo;

    @TableField("eto_plant")
    private String etoPlant;

    @TableField("stock_movement_date")
    private String stockMovementDate;


    @TableField("country")
    private String country;

    @TableField("prod_order")
    private String prodOrder;

    @TableField("qty")
    private Integer qty;

    @TableField("is_disassembled")
    private String isDisassembled;

    @TableField("tr")
    private String tr;

    @TableField("sap_code")
    private String sapCode;

    @TableField("pallet_quantity")
    private String palletQuantity;

    @TableField("after_packing")
    private String afterPacking;
    /**
     * 状态（1：启用，0：停用）
     */
    private Integer status;


    /**
     * 删除标记1：删除，0:可用
     */
    private Integer deleteFlag;
}