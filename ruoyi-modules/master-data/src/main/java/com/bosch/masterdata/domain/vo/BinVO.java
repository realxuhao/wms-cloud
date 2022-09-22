package com.bosch.masterdata.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BinVO {
    /** 仓库代码 */
    @ApiModelProperty(value = "仓库代码")
    private String wareCode;

    /** 仓库名称 */
    @ApiModelProperty(value = "仓库名称")
    private String wareName;

    /** 仓库类型，1：内库，0：外库 */
    @ApiModelProperty(value = "仓库类型，1：内库，0：外库 ")
    private Long wareType;

    /** 区域代码 */
    @ApiModelProperty(value = "区域代码")
    private String areaCode;

    /** 区域名称 */
    @ApiModelProperty(value = "区域名称")
    private String areaName;

    /** 货架代码 */
    @ApiModelProperty(value = "货架代码")
    private String shelveCode;

    /** 货架名称 */
    @ApiModelProperty(value = "货架名称")
    private String shelveName;

    /** 跨编码 */
    @ApiModelProperty(value = "跨编码")
    private String code;

    /** 宽度 */
    @ApiModelProperty(value = "宽度")
    private BigDecimal width;

    /** 承重 */
    @ApiModelProperty(value = "承重")
    private BigDecimal bearWeight;

    /** 库位编码 */
    @ApiModelProperty(value = "库位编码")
    private String binCode;

}
