package com.bosch.masterdata.api.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DepartmentVO {
    /** 部门id */
    @ApiModelProperty(value = "部门id")
    private Long id;

    /** 部门名称 */
    @ApiModelProperty(value = "部门名称")
    private String name;

    /** 部门编码 */
    @ApiModelProperty(value = "部门编码")
    private String code;

}
