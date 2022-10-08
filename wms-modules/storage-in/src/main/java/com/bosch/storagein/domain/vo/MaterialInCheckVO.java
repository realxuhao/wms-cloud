package com.bosch.storagein.domain.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MaterialInCheckVO {

    /**
     * SSCC码
     */
    @ApiModelProperty(value = "SSCC码")
    private String ssccNumber;

    /**
     * 物料号
     */
    @ApiModelProperty(value = "物料号")
    private String materialNb;

    /**
     * 批次号
     */
    @ApiModelProperty(value = "批次号")
    private String batchNb;

    /**
     * 应检查数量
     */
    @ApiModelProperty(value = "应检查数量")
    private Integer checkQuantity;

    /**
     * 最小标准
     */
    @ApiModelProperty(value = "最小标准")
    private Double minStandard;

    /**
     * 最大标准
     */
    @ApiModelProperty(value = "最大标准")
    private Double maxStandard;

    /**
     * 检验类型
     */
    @ApiModelProperty(value = "检验类型，0：称重，1：数数，2：免检，3：该批次已检")
    private Integer checkType;

    /**
     * 校验类型描述
     */
    @ApiModelProperty(value = "校验类型描述")
    private String checkTypeDesc;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位")
    private String unit;

    /**
     * 来源PO号
     */
    @ApiModelProperty(value = "来源PO号")
    private String fromPurchaseOrder;

    /**
     * PO行号
     */
    @ApiModelProperty(value = "PO行号")
    private String poNumberItem;

}
