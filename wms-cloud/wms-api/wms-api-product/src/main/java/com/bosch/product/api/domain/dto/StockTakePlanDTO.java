package com.bosch.product.api.domain.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.web.page.PageDomain;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.util.Date;

@Data
public class StockTakePlanDTO extends PageDomain {
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Integer id;

    /**
     * 盘点计划编码
     */
@ApiModelProperty(value = "盘点计划编码")
    private String code;

    /**
     * 删除标记1：删除，0:可用
     */
    @ApiModelProperty(value = "删除标记1：删除，0:可用")
    private Integer deleteFlag;

    /**
     * 盘点cell
     */
    @ApiModelProperty(value = "盘点cell")
    private String cell;

    /**
     * 盘点仓库
     */
    @ApiModelProperty(value = "盘点仓库")
    private String wareCode;

    /**
     * 盘点区域
     */
    @ApiModelProperty(value = "盘点区域")
    private String areaCode;

    /**
     * 盘点类型，0:明盘，1:盲盘
     */
    @ApiModelProperty(value = "盘点类型，0:明盘，1:盲盘")
    private Integer type;

    /**
     * 盘点方式
     */
    @ApiModelProperty(value = "盘点方式")
    private Integer method;

    /**
     * 创建时物料总数
     */
    @ApiModelProperty(value = "创建时物料总数")
    private Integer createdMaterialQuantity;

    /**
     * 第一次下发时物料总数
     */
    @ApiModelProperty(value = "第一次下发时物料总数")
    private Integer firstIssueMaterialQuantity;

    /**
     * 第二次下发时物料总数
     */
    @ApiModelProperty(value = "第二次下发时物料总数")
    private Integer secondIssueMaterialQuantity;

    /**
     * 第三次下发时物料总数
     */
    @ApiModelProperty(value = "第三次下发时物料总数")
    private Integer thirdIssueMaterialQuantity;

    /**
     * 总下发物料总数
     */
    @ApiModelProperty(  value = "总下发物料总数")
    private Integer totalIssueQuantity;

    /**
     * 判断库位总数
     */
    @ApiModelProperty(value = "判断库位总数")
    private Integer takeBinQuantity;

    /**
     * 差异库位数
     */
    @ApiModelProperty(value = "差异库位数")
    private Integer diffBinQuantity;

    /**
     * 0:已创建，1:进行中，2:完成
     */
    @ApiModelProperty(value = "0:已创建，1:进行中，2:完成")
    private Integer status;

    /**
     * 物料类型：0:原材料，1:成品
     */
    @ApiModelProperty(value = "物料类型：0:原材料，1:成品")
    private Integer takeMaterialType;

    @ApiModelProperty(value = "循环盘点月份")
    private Integer circleTakeMonth;

    /** 创建者 */
    @ApiModelProperty(value = "创建者")
    private String createBy;

    /** 创建时间起 */
    @ApiModelProperty(value = "创建时间起")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeStart;

    /** 创建时间止 */
    @ApiModelProperty(value = "创建时间止")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeEnd;
}
