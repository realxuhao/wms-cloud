package com.bosch.masterdata.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WareVO {
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 仓库编码
     */
    @ApiModelProperty(value = "仓库编码")
    private String code;

    /**
     * 仓库名称
     */
    @ApiModelProperty(value = "仓库名称")
    private String name;

    /**
     * 仓库地址
     */
    @ApiModelProperty(value = "仓库地址")
    private String location;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private String createTime;


}
