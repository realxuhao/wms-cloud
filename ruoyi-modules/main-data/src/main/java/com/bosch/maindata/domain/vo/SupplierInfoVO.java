package com.bosch.maindata.domain.vo;

import com.ruoyi.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SupplierInfoVO {

    /** id */
    @ApiModelProperty(value = "id")
    private Long id;

    /** 供应商编码 */
    @ApiModelProperty(value = "供应商编码")
    private String code;

    /** 供应商名称 */
    @ApiModelProperty(value = "供应商名称")
    private String name;

    /** 供应商时间窗口 */
    @ApiModelProperty(value = "供应商时间窗口")
    private Long timeWindow;

    /** 状态（1：启用，0：停用） */
    @ApiModelProperty(value = "状态（1：启用，0：停用）")
    private Long status;
}
