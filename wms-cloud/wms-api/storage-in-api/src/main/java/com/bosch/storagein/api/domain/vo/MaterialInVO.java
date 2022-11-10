package com.bosch.storagein.api.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author: UWH4SZH
 * @since: 10/8/2022 14:57
 * @description:
 */
@Data
public class MaterialInVO {
    /**
     * id
     */
    @ApiModelProperty(value = "主键id")
    private Long id;

    /**
     * 工厂
     */
    @ApiModelProperty(value = "工厂")
    private String plantNb;

    /**
     * 仓库编码
     */
    @ApiModelProperty(value = "仓库编码")
    private String wareCode;

    /**
     * SSCC码
     */
    @ApiModelProperty(value = "SSCC码")
    private String ssccNumber;

    /**
     * 批次号
     */
    @ApiModelProperty(value = "批次号")
    private String batchNb;

    /**
     * 检验类型
     */
    @ApiModelProperty(value = "检验类型，0：称重，1：数数，2：免检，3：该批次已检")
    private Integer checkType;

    /**
     * 物料号
     */
    @ApiModelProperty(value = "物料号")
    private String materialNb;

    @ApiModelProperty(value = "物料名称")
    private String materialName;


    /**
     * 应检查数量
     */
    @ApiModelProperty(value = "目标抽样件数")
    private Integer checkQuantity;

    /**
     * 最小标准
     */
    @ApiModelProperty(value = "最小标准")
    private Integer minStandard;

    /**
     * 最大标准
     */
    @ApiModelProperty(value = "最大标准")
    private Integer maxStandard;

    /**
     * 实际数量
     */
    @ApiModelProperty(value = "实际抽样数量")
    private Integer actualQuantity;

    /**
     * 实际称重、数数结果
     */
    @ApiModelProperty(value = "实际称重、数数结果")
    private Double actualResult;

    /**
     * 实际平均结果
     */
    @ApiModelProperty(value = "实际平均结果")
    private Double averageResult;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private String operateUser;

    /**
     * 操作时间
     */
    @ApiModelProperty(value = "操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date operateTime;

    /**
     * 原托数
     */
    @ApiModelProperty(value = "原托数")
    private Integer originalPalletQuantity;
    /**
     * 该托的物料数量
     */
    @ApiModelProperty(value = "该托的物料数量")
    private Integer quantity;


    /**
     * 来源PO号
     */
    @ApiModelProperty(value = "来源PO号")
    private String fromPurchaseOrder;


}
