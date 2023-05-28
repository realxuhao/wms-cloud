package com.bosch.masterdata.api.domain;

import java.math.BigDecimal;


import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * 物料信息对象 md_material
 * 
 * @author xuhao
 * @date 2022-09-22
 */
@Data
@TableName("md_material")
public class Material extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 物料类型代码 */
    @Excel(name = "物料类型代码")
    private String code;

    /** 物料名称 */
    @Excel(name = "物料名称")
    private String name;

    /** 最小包装数量 */
    @Excel(name = "最小包装数量")
    private Double minPackageNumber;

    /** 物料类型id */
    @Excel(name = "物料类型id")
    private Long materialTypeId;

    /** 单位 */
    @Excel(name = "单位")
    private String unit;

    /** 物料防错方式 */
    @Excel(name = "物料防错方式")
    private String errorProofingMethod;

    /** 是否带托盘 */
    @Excel(name = "是否带托盘")
    private Long hasPallet;

    /** 是否绑定托盘 */
    @Excel(name = "是否绑定托盘")
    private Long bindPallet;

    /** 包装重量 */
    @Excel(name = "包装重量")
    private Double packageWeight;

    /** 托盘重量 */
    @Excel(name = "托盘重量")
    private Double palletWeight;

    /** 整托移库标记 */
    @Excel(name = "整托移库标记")
    private String wholeShiftFlag;

    /** iqc */
    @Excel(name = "IQC")
    private String iqc;

    /** 来料总重量（每托） */
    @Excel(name = "来料总重量", readConverterExp = "每=托")
    private Double totalWeight;

    /** 最小包装重量(净重) */
    @Excel(name = "最小包装重量(净重)")
    private BigDecimal minPackageNetWeight;

    /** 允许负偏差比例【绝对值】 */
    @Excel(name = "允许负偏差比例【绝对值】")
    private BigDecimal lessDeviationRatio;

    /** 允许正偏差比例【绝对值】 */
    @Excel(name = "允许正偏差比例【绝对值】")
    private BigDecimal moreDeviationRatio;

    /** 状态（1：启用，0：停用） */
    @Excel(name = "状态", readConverterExp = "1=：启用，0：停用")
    private Long status;

    /**
     * 转化重量参数
     */
    @ApiModelProperty(value = "转化重量参数")
    private BigDecimal transferWeightRatio;

    /**
     * 托盘id
     */
    @ApiModelProperty(value = "托盘id")
    private Long palletId;

    /**
     * 托盘类型
     */
    @ApiModelProperty(value = "托盘类型")
    private String palletType;
    /**
     * deleteFlag
     */
    @ApiModelProperty(value = "deleteFlag")
    private Integer deleteFlag;


}
