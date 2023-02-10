package com.bosch.binin.api.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-02-07 10:08
 **/
@Data
public class NMDIQCRuleDTO {
    @ApiModelProperty(value = "数量")
    private Double quantity;
    @ApiModelProperty(value = "检验水平")
    private String checkLevel;
    @ApiModelProperty(value = "水平类型，1：正常，2：加严，3：放宽")
    private Integer levelType;

}
