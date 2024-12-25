package com.bosch.binin.api.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ChangeBinDTO {

    @ApiModelProperty(value = "sscc码")
    private String sscc;

    @ApiModelProperty(value = "库位编号")
    private String bin;
}
