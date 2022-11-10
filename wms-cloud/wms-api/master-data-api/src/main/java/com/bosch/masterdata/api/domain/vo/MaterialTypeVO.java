package com.bosch.masterdata.api.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class MaterialTypeVO {

    @ApiModelProperty(value = "物料类型id")
    private Long id;

    /**
     * 物料类型代码
     */
    @ApiModelProperty(value = "物料类型代码")
    private String code;

    /**
     * 物料类型描述
     */
    @ApiModelProperty(value = "物料类型描述")
    private String description;

    /**
     * 所属部门
     */
    @ApiModelProperty(value = "所属部门")
    private Long departmentId;

    /**
     * 所属部门名称
     */
    @ApiModelProperty(value = "所属部门名称")
    private String departmentName;

    /**
     * 状态（1：启用，0：停用）
     */
    @ApiModelProperty(value = "状态（1：启用，0：停用）")
    private Long status;


    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
