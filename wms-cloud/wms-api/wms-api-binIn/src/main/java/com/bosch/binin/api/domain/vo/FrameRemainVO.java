package com.bosch.binin.api.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class FrameRemainVO {

    private BigDecimal frameWidth;

    private BigDecimal frameBearWeight;

    @ApiModelProperty(value = "推荐库位")
    private String recommendBinCode;
}
