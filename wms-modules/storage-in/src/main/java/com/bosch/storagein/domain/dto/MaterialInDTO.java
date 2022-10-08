package com.bosch.storagein.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author: UWH4SZH
 * @since: 10/8/2022 14:39
 * @description:
 */
@Data
public class MaterialInDTO {

    /**
     * id
     */
    @ApiModelProperty(value = "主键id")
    private Long id;

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
     * 物料号
     */
    @ApiModelProperty(value = "物料号")
    private String materialNb;

    /**
     * 检查类型
     */
    @ApiModelProperty(value = "检查类型")
    private Integer checkType;



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
     * 实际数量
     */
    @ApiModelProperty(value = "实际数量")
    private Integer actualQuantity;

    /**
     * 实际称重、数数结果
     */
    @ApiModelProperty(value = "实际称重、数数结果")
    private Double actualResult;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private String operateUser;

    /**
     * 操作时间
     */
    @ApiModelProperty(value = "操作时间")
    private Date operateTime;
}
