package com.bosch.masterdata.api.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class MaterialVO {

    /** id */
    @ApiModelProperty(value = "物料id")
    private Long id;

    /** 物料代码 */
    @ApiModelProperty(value = "物料代码")
    private String code;

    /** 物料名称 */
    @ApiModelProperty(value = "物料名称")
    private String name;

    /** 最小包装数量 */
    @ApiModelProperty(value = "最小包装数量")
    private Long minPackageNumber;

    /** 物料类型id */
    @Excel(name = "物料类型id")
    private Long materialTypeId;

    /** 物料类型 */
    @ApiModelProperty(value = "物料类型")
    private String materialType;

    /** 单位 */
    @ApiModelProperty(value = "\"单位\n" +
            "[KG,M,L,㎡]\"")
    private String unit;

    /** 物料防错方式 */
    @ApiModelProperty(value = "\"物料防错方式\n" +
            "[点数,称重,免检]\"")
    private String errorProofingMethod;

    /** 是否带托盘 */
    @ApiModelProperty(value = "是否带托盘")
    private Integer hasPallet;

    /** 是否绑定托盘 */
    @ApiModelProperty(value = "是否绑定托盘")
    private Integer bindPallet;

    /** 包装重量 */
    @ApiModelProperty(value = "\"最小包装毛重\n" +
            "[只针对称重物料]\"")
    private BigDecimal packageWeight;

    /** 托盘重量 */
    @ApiModelProperty(value = "\"托盘重量\n" +
            "[只针对称重物料]\"")
    private BigDecimal palletWeight;

    /** 来料总重量（每托） */
    @ApiModelProperty(value = "来料总重量")
    private BigDecimal totalWeight;

    /** 最小包装重量(净重) */
    @ApiModelProperty(value = "\"最小包装净重\n" +
            "[只针对称重物料]\"")
    private BigDecimal minPackageNetWeight;

    /** 允许负偏差比例【绝对值】 */
    @ApiModelProperty(value = "\"最小包装量\n" +
            "允许最小值\"")
    private BigDecimal lessDeviationRatio;

    /** 允许正偏差比例【绝对值】 */
    @ApiModelProperty(value = "\"最小包装量\n" +
            "允许最大值\"")
    private BigDecimal moreDeviationRatio;

    /**转化重量参数 */
    @ApiModelProperty(value = "标准计数单位[L,Kg,m,㎡]对应的重量值[只针对称重物料]")
    private BigDecimal transferWeightRatio;

    /** 状态（1：启用，0：停用） */
    @ApiModelProperty(value = "状态 1：启用，0：停用")
    private Long status;

    @ApiModelProperty(value = "备注")
    /** 备注 */
    private String remark;

    /** 创建时间 */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 托盘类型
     */
    @ApiModelProperty(value = "托盘类型")
    private String palletType;

    /**
     * 托盘id
     */
    @ApiModelProperty(value = "托盘id")
    private Long palletId;
}
