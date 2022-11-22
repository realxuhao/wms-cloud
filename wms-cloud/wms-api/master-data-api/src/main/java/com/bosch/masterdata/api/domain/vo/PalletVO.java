package com.bosch.masterdata.api.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PalletVO {
    /** id */
    @ApiModelProperty(value = "id")
    private Long id;

    /** 托盘类型 */
    @ApiModelProperty(value = "托盘类型")
    private String type;

    /** 托盘长度 */
    @ApiModelProperty(value = "托盘长度")
    private BigDecimal length;

    /** 托盘宽度 */
    @ApiModelProperty(value = "托盘宽度")
    private BigDecimal width;

    /** 托盘高度 */
    @ApiModelProperty(value = "托盘高度")
    private BigDecimal height;

    /** 虚拟托盘前缀编码 */
    @ApiModelProperty(value = "虚拟托盘前缀编码")
    private String virtualPrefixCode;

    /** 状态（1：启用，0：停用） */
    @ApiModelProperty(value = "状态（1：启用，0：停用）")
    private Long status;


    /**
     * 删除标记1：删除，0:可用
     */
    @ApiModelProperty(value = "删除标记1：删除，0:可用")
    private Integer deleteFlag;

    /**
     * 是否是虚拟托盘 1：实物，0:虚拟
     */
    @ApiModelProperty(value = "是否是虚拟托盘 0：实物，1:虚拟")
    private Integer isVirtual;
}
