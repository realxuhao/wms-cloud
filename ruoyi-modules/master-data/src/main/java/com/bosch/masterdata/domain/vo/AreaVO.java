package com.bosch.masterdata.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AreaVO {
    /** id */
    @ApiModelProperty(value = "id")
    private Long id;

    /** 仓库id */
    @ApiModelProperty(value = "仓库id")
    private Long wareId;

    /** 存储区编码 */
    @ApiModelProperty(value = "存储区编码")
    private String code;

    /** 存储区名称 */
    @ApiModelProperty(value = "存储区名称")
    private String name;

    /** 备注 */
    @ApiModelProperty(value = "备注")
    private String remark;
}
