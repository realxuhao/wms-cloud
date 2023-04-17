package com.bosch.product.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.ruoyi.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * @TableName stock_take_plan
 */
@TableName(value ="stock_take_plan")
@Data
public class StockTakePlan extends BaseEntity implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 盘点计划编码
     */
    @TableField(value = "code")
    private String code;

    /**
     * 删除标记1：删除，0:可用
     */
    @TableField(value = "delete_flag")
    private Integer deleteFlag;

    /**
     * 盘点cell
     */
    @TableField(value = "cell")
    private String cell;

    /**
     * 盘点仓库
     */
    @TableField(value = "ware_code")
    private String wareCode;

    /**
     * 盘点区域
     */
    @TableField(value = "area_code")
    private String areaCode;

    /**
     * 盘点类型，0:明盘，1:盲盘
     */
    @TableField(value = "type")
    private int type;

    /**
     * 盘点方式
     */
    @TableField(value = "method")
    private int method;

    /**
     * 创建时物料总数
     */
    @TableField(value = "created_material_quantity")
    private Integer createdMaterialQuantity;

    /**
     * 第一次下发时物料总数
     */
    @TableField(value = "first_issue_material_quantity")
    private Integer firstIssueMaterialQuantity;

    /**
     * 第二次下发时物料总数
     */
    @TableField(value = "second_issue_material_quantity")
    private Integer secondIssueMaterialQuantity;

    /**
     * 第三次下发时物料总数
     */
    @TableField(value = "third_issue_material_quantity")
    private Integer thirdIssueMaterialQuantity;

    /**
     * 总下发物料总数
     */
    @TableField(value = "total_issue_quantity")
    private Integer totalIssueQuantity;

    /**
     * 判断库位总数
     */
    @TableField(value = "take_bin_quantity")
    private Integer takeBinQuantity;

    /**
     * 差异库位数
     */
    @TableField(value = "diff_bin_quantity")
    private Integer diffBinQuantity;

    /**
     * 0:已创建，1:进行中，2:完成
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 物料类型：0:原材料，1:成品
     */
    @TableField(value = "take_material_type")
    private Integer takeMaterialType;

    @TableField(value = "circle_take_month")
    private Integer circleTakeMonth;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}