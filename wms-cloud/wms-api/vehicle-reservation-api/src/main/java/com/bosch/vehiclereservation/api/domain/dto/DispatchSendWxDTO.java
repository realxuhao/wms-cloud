package com.bosch.vehiclereservation.api.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DispatchSendWxDTO {

    @ApiModelProperty(value = "微信token")
    private String wxToken;

    @ApiModelProperty(value = "主键id")
    private Long dispatchId;

    @ApiModelProperty(value = "微信openid")
    private String wechatId;

    @ApiModelProperty(value = "仓库编码")
    private String wareCode;
    @ApiModelProperty(value = "仓库名称")
    private String wareName;

    @ApiModelProperty(value = "道口编号")
    private String dockCode;

}
