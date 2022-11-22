package com.bosch.masterdata.api.domain.dto;

import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.web.page.PageDomain;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class PalletDTO extends PageDomain {

    /** id */
    @ApiModelProperty(value = "id")
    private Long id;

    /** 托盘类型 */
    @NotEmpty(message = "托盘类型不能为空")
    @ApiModelProperty(value = "托盘类型")
    private String type;

    /** 托盘长度 */
    @NotNull(message = "托盘长度不能为空")
    @DecimalMin("0")
    @ApiModelProperty(value = "托盘长度")
    private BigDecimal length;

    /** 托盘宽度 */
    @NotNull(message = "托盘宽度不能为空")
    @ApiModelProperty(value = "托盘宽度")
    @DecimalMin("0")
    private BigDecimal width;

    /** 托盘高度 */
    @NotNull(message = "托盘高度不能为空")
    @ApiModelProperty(value = "托盘高度")
    @DecimalMin("0")
    private BigDecimal height;

    /** 虚拟托盘前缀编码 */
    @ApiModelProperty(value = "虚拟托盘前缀编码")
    private String virtualPrefixCode;

    /** 状态（1：启用，0：停用） */
    @ApiModelProperty(value = "状态（1：启用，0：停用）")
    private Long status;

    /**
     * 是否是虚拟托盘 1：实物，0:虚拟
     */
    @NotNull(message = "是否是虚拟托盘不能为空")
    @ApiModelProperty(value = "是否是虚拟托盘 0：实物，1:虚拟")
    private Integer isVirtual;
}
