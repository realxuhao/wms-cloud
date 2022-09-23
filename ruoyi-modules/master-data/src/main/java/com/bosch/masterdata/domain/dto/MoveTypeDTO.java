package com.bosch.masterdata.domain.dto;

import com.ruoyi.common.core.web.page.PageDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MoveTypeDTO extends PageDomain {
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 移动类型
     */
    @ApiModelProperty(value = "移动类型")
    private String type;

    /**
     * 移动类型编码
     */
    @ApiModelProperty(value = "移动类型编码")
    private String code;

    /**
     * 移动类型描述
     */
    @ApiModelProperty(value = "移动类型描述")
    private String description;

    @ApiModelProperty(value = "状态（1：启用，0：停用）")
    private Long status;

}
