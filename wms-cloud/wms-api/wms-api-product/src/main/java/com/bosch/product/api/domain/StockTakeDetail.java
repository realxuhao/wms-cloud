package com.bosch.product.api.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.DataOutput;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * @TableName stock_take_detail
 */
@TableName(value ="stock_take_detail")
@Data
public class StockTakeDetail extends BaseEntity implements Serializable {
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 计划编码
     */
    @TableField(value = "plan_code")
    private String planCode;

    /**
     * 物料编码
     */
    @TableField(value = "material_code")
    private String materialCode;

    /**
     * sscc
     */
    @TableField(value = "sscc_nb")
    private String ssccNb;

    /**
     * 批次号
     */
    @TableField(value = "batch_nb")
    private String batchNb;

    /**
     * 仓库编码
     */
    @TableField(value = "ware_code")
    private String wareCode;

    /**
     * 区域编码
     */
    @TableField(value = "area_code")
    private String areaCode;

    /**
     * 库位编码
     */
    @TableField(value = "bin_code")
    private String binCode;

    /**
     * 循环盘点月份
     */
    @TableField(value = "circle_take_month")
    private Integer circleTakeMonth;

    /**
     * 状态
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 库存数量
     */
    @TableField(value = "stock_quantity")
    private Double stockQuantity;

    /**
     * 盘点数量
     */
    @TableField(value = "take_quantity")
    private Double takeQuantity;

    /**
     * 0:无差异，1:有差异
     */
    @TableField(value = "is_diff")
    private Integer isDiff;

    /**
     * 任务编码
     */
    @TableField(value = "task_no")
    private String taskNo;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** 创建时间 */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date issueTime;



}