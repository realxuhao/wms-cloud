package com.bosch.masterdata.api.domain.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.web.page.PageDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MaterialDTO extends PageDomain {
    /**
     * id
     */
    @ExcelProperty(value = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 物料类型代码
     */
    @ExcelProperty(value = "物料代码")
    @ApiModelProperty(value = "物料代码")
    private String code;

    /**
     * 物料名称
     */
    @ExcelProperty(value = "物料名称")
    @ApiModelProperty(value = "物料名称")
    private String name;

    /**
     * 最小包装数量
     */
    @ExcelProperty(value = "最小包装数量")
    @ApiModelProperty(value = "最小包装数量")
    private Long minPackageNumber;

    /**
     * 物料类型id
     */
    @ExcelProperty(value = "物料类型id")
    @Excel(name = "物料类型id")
    private Long materialTypeId;

    /**
     * 物料类型
     */
    @ExcelProperty(value = "物料类型")
    @ApiModelProperty(value = "物料类型")
    private String materialType;

    /**
     * 单位
     */
    @ExcelProperty(value = "单位")
    @ApiModelProperty(value = "单位")
    private String unit;

    /**
     * 物料防错方式
     */
    @ExcelProperty(value = "物料防错方式")
    @ApiModelProperty(value = "物料防错方式")
    private String errorProofingMethod;

    /**
     * 是否带托盘
     */
    @ExcelProperty(value = "是否带托盘")
    @ApiModelProperty(value = "是否带托盘")
    private Long hasPallet;

    /**
     * 是否绑定托盘
     */
    @ExcelProperty(value = "是否绑定托盘")
    @ApiModelProperty(value = "是否绑定托盘")
    private Long bindPallet;

    /**
     * 包装重量
     */
    @ExcelProperty(value = "包装重量")
    @ApiModelProperty(value = "包装重量")
    private Long packageWeight;

    /**
     * 托盘重量
     */
    @ExcelProperty(value = "托盘重量")
    @ApiModelProperty(value = "托盘重量")
    private Long palletWeight;

    /**
     * 来料总重量（每托）
     */
    @ExcelProperty(value = "来料总重量")
    @ApiModelProperty(value = "来料总重量")
    private Long totalWeight;

    /**
     * 最小包装重量(净重)
     */
    @ExcelProperty(value = "最小包装重量(净重)")
    @ApiModelProperty(value = "最小包装重量(净重)")
    private BigDecimal minPackageNetWeight;

    /**
     * 允许负偏差比例【绝对值】
     */
    @ExcelProperty(value = "允许负偏差比例【绝对值】")
    @ApiModelProperty(value = "允许负偏差比例【绝对值】")
    private BigDecimal lessDeviationRatio;

    /**
     * 转化重量参数
     */
    @ApiModelProperty(value = "转化重量参数")
    private BigDecimal transferWeightRatio;

    /**
     * 允许正偏差比例【绝对值】
     */
    @ExcelProperty(value = "允许正偏差比例【绝对值】")
    @ApiModelProperty(value = "允许正偏差比例【绝对值】")
    private BigDecimal moreDeviationRatio;

    /**
     * 状态（1：启用，0：停用）
     */
    @ExcelProperty(value = "状态 1：启用，0：停用")
    @ApiModelProperty(value = "状态 1：启用，0：停用")
    private Long status;

    @ApiModelProperty(value = "备注")
    @ExcelProperty(value = "备注")
    /** 备注 */
    private String remark;
}
