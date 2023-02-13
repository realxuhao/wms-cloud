package com.bosch.masterdata.api.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BlackDriverVO {

    /**
     * 主键id
     */
    @ApiModelProperty(value = "id")
    private Long driverId;

    /**
     * 司机姓名
     */
    @ApiModelProperty(value = "司机姓名")
    private String driverName;

    /**
     * 状态：1：启用黑名单 0：禁用黑名单
     */
    @ApiModelProperty(value = "状态：1：启用黑名单 0：禁用黑名单")
    private Integer status;

}
