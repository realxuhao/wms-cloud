package com.bosch.masterdata.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class MoveTypeVO {
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


    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


    @ApiModelProperty(value = "状态（1：启用，0：停用）")
    private Long status;
}
