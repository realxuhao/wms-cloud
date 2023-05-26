package com.bosch.product.api.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.DataOutput;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.annotation.Excel;
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
    @Excel(name = "工厂编码")
    private String planCode;

    /**
     * 物料编码
     */
    @TableField(value = "material_code")
    @Excel(name = "物料号")
    private String materialCode;

    /**
     * sscc
     */
    @TableField(value = "sscc_nb")
    @Excel(name = "sscc")
    private String ssccNb;

    /**
     * 批次号
     */
    @TableField(value = "batch_nb")
    @Excel(name = "批次号")
    private String batchNb;

    /**
     * 仓库编码
     */
    @TableField(value = "ware_code")
    @Excel(name = "仓库编码")
    private String wareCode;

    /**
     * 区域编码
     */
    @TableField(value = "area_code")
    @Excel(name = "区域编码")
    private String areaCode;

    /**
     * 库位编码
     */
    @TableField(value = "bin_code")
    @Excel(name = "库位编码")
    private String binCode;

    /**
     * 循环盘点月份
     */
    @TableField(value = "circle_take_month")
    @Excel(name = "循环盘点月份")
    private Integer circleTakeMonth;

    /**
     * 状态
     */
    @TableField(value = "status")
    @Excel(name = "状态",readConverterExp = "0:待下发,1:待盘点,2:待确认,3:完成")
    private Integer status;

    /**
     * 库存数量
     */
    @TableField(value = "stock_quantity")
    @Excel(name = "库存数量")
    private Double stockQuantity;

    /**
     * 盘点数量
     */
    @TableField(value = "take_quantity")
    @Excel(name = "盘点数量")
    private Double takeQuantity;

    /**
     * 0:无差异，1:有差异
     */
    @TableField(value = "is_diff")
    @Excel(name = "库存数量",readConverterExp = "0:无差异,1:有差异")
    private Integer isDiff;

    /**
     * 任务编码
     */
    @TableField(value = "task_no")
    @Excel(name = "任务编码")
    private String taskNo;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** 下发时间 */
    @ApiModelProperty(value = "下发时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "下发时间")
    private Date issueTime;


    @ApiModelProperty(value = "盘点人")
    @Excel(name = "盘点人")
    private String takeBy;


    @ApiModelProperty(value = "盘点时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "盘点时间")
    private Date takeTime;

    @ApiModelProperty(value = "调整人")
    @Excel(name = "调整人")
    private String editBy;

        @ApiModelProperty(value = "调整时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @Excel(name = "调整时间")
    private Date editTime;


}