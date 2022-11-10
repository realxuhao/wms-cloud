package com.bosch.binin.api.domain.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;

public class MaterialCallDTO {

    /**
     * id
     */
    @ExcelProperty( "id")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 订单号
     */
    @ExcelProperty( "订单号")
    @ApiModelProperty(value = "订单号")
    private String oderNb;

    /**
     * 物料号
     */
    @ExcelProperty( "物料代码")
    @ApiModelProperty(value = "物料代码")
    private String materialNb;

    /**
     * 物料名称
     */
    @ExcelProperty( "物料名称")
    @ApiModelProperty(value = "物料名称")
    private String materialName;

    /**
     * 数量
     */
    @ExcelProperty( "数量")
    @ApiModelProperty(value = "数量")
    private Double quantity;

    /**
     * 单位
     */
    @ExcelProperty( "单位")
    @ApiModelProperty(value = "单位")
    private String unit;


    /**
     * 备注
     */
    @ExcelProperty( "备注")
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 部门
     */
    @ExcelProperty( "部门")
    @ApiModelProperty(value = "部门")
    private String cell;

    /**
     * 排序类型
     */
    @ExcelProperty( "排序类型")
    @ApiModelProperty(value = "排序类型")
    private Integer sortType;

    /**
     * 删除标记，0：可用，1：已删除
     */
    @ExcelProperty( "删除标记")
    @ApiModelProperty(value = "删除标记")
    private Integer deleteFlag;
}
