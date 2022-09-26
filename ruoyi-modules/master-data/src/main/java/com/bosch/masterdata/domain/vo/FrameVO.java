package com.bosch.masterdata.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
//跨
public class FrameVO {

    /** id */
    @ApiModelProperty(value = "id")
    private Long id;

    /** 仓库id */
    @ApiModelProperty(value = "id")
    private Long wareId;

    /** 仓库编码 */
    @ApiModelProperty(value = "仓库编码")
    private String wareCode;

    /** 仓库名称 */
    @ApiModelProperty(value = "仓库名称")
    private String wareName;

    /** 区域id */
    @ApiModelProperty(value = "区域id")
    private Long areaId;

    /** 区域名称 */
    @ApiModelProperty(value = "区域名称")
    private Long areaName;

    /** 区域编码 */
    @ApiModelProperty(value = "区域编码")
    private Long areaCode;

    /** 跨编码 */
    @ApiModelProperty(value = "跨编码")
    private String code;

    /** 跨名称 */
    @ApiModelProperty(value = "跨名称")
    private String name;

    /** 宽度 */
    @ApiModelProperty(value = "宽度")
    private BigDecimal width;

    /** 承重 */
    @ApiModelProperty(value = "承重")
    private BigDecimal bearWeight;
}
