package com.bosch.masterdata.api.domain.dto;

import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.web.page.PageDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BinDTO extends PageDomain {

    /** id */
    @ApiModelProperty(value = "id")
    private Long id;

    /** 跨id */
    @ApiModelProperty(value = "跨id")
    private Long frameId;

    /** 库位编码 */
    @ApiModelProperty(value = "库位编码")
    private String code;

    /** 库位名称 */
    @ApiModelProperty(value = "库位名称")
    private String name;

    /** 状态（1：启用，0：停用） */
    @ApiModelProperty(value = "状态（1：启用，0：停用）")
    private Long status;
}
