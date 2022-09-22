package com.bosch.masterdata.domain.dto;

import com.ruoyi.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;

public class BinDTO {

    /** id */
    @ApiModelProperty(value = "id")
    private Long id;

    /** 跨id */
    @ApiModelProperty(value = "跨id")
    private Long frameId;

    /** 库位编码 */
    @ApiModelProperty(value = "库位编码")
    private String code;

    /** 状态（1：启用，0：停用） */
    @ApiModelProperty(value = "状态（1：启用，0：停用）")
    private Long status;
}
