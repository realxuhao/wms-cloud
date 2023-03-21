package com.bosch.product.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.ruoyi.common.core.web.domain.BaseEntity;
import lombok.Data;

/**
 * 
 * @TableName shipping_task
 */
@TableName(value ="shipping_task")
@Data
public class ShippingTask extends BaseEntity {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    private String packageNo;
    /**
     * plan表id
     */
    private String shippingPlanId;

    /**
     * 
     */
    private String shippingMark;

    /**
     * 
     */
    private String etoPo;

    /**
     * 
     */
    private String etoPlant;

    /**
     * 
     */
    private String stockMovementDate;

    /**
     * 
     */
    private String country;

    /**
     * 
     */
    private String prodOrder;

    /**
     * 
     */
    private Integer qty;

    /**
     * 
     */
    private String isDisassembled;

    /**
     * 
     */
    private String tr;

    /**
     * 
     */
    private String sapCode;

    /**
     * 
     */
    private String palletQuantity;

    /**
     * 
     */
    private String afterPacking;



    /**
     * 状态（2：已执行，1:执行中,0：未执行）
     */
    private Integer status;

    /**
     * 删除标记1：删除，0:可用
     */
    private Integer deleteFlag;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}