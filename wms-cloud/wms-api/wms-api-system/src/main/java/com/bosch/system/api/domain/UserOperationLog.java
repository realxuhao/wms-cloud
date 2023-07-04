package com.bosch.system.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.Data;

/**
 * 用户操作记录表
 * @TableName user_operation_log
 */
@TableName(value ="user_operation_log")
@Data
public class UserOperationLog implements Serializable {
    /**
     * 
     */
    @TableField(value = "id")
    private Long id;

    /**
     * 0：物料；1：成品
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 订单号
     */
    @TableField(value = "order_number")
    private String orderNumber;

    /**
     * 创建人
     */
    @TableField(value = "create_by")
    private String createBy;

    /**
     * 修改人
     */
    @TableField(value = "update_by")
    private String updateBy;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

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

    /**
     * 物料、成品号
     */
    @TableField(value = "code")
    private String code;

    /**
     * 
     */
    @TableField(value = "sscc_number")
    private String ssccNumber;

    /**
     * 
     */
    @TableField(value = "operation_type")
    private Integer operationType;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}