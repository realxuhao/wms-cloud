package com.bosch.product.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.ruoyi.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * @TableName shipping_history
 */
@TableName(value ="shipping_history")
@Data
public class ShippingHistory extends BaseEntity {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     *
     */
    @TableField(value = "history_index")
    private Integer historyIndex;

    /**
     * task表id
     */
    @TableField(value = "shipping_task_id")
    private Long shippingTaskId;

    /**
     * 
     */
    @TableField(value = "eto_po")
    private String etoPo;

    @TableField(value = "sscc_numbers")
    private String ssccNumbers;
    /**
     * 
     */
    @TableField(value = "prod_order")
    private String prodOrder;


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