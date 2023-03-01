package com.bosch.vehiclereservation.api.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

@Data
public class WxMsgDTO {

    @ApiModelProperty(value = "openid")
    private String touser;

    @ApiModelProperty(value = "消息模板id")
    private String template_id;

    @ApiModelProperty(value = "page")
    private String page;

    @ApiModelProperty(value = "消息")
    private Map<String, Map<String, String>> data;


}
