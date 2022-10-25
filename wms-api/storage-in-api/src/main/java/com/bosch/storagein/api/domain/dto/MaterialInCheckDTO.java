package com.bosch.storagein.api.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.PrintWriter;

@Data
public class MaterialInCheckDTO {

    @ApiModelProperty(value = "SSCC码")
    private String mesBarCode;

    @ApiModelProperty(value = "实际数量")
    private Integer actualQuantity;

    @ApiModelProperty(value = "实际称重、数数结果")
    private Double actualResult;

    @ApiModelProperty(value = "原托数")
    private Integer originPalletQuantity;

    @ApiModelProperty(value = "称重次数")
    private Integer weightTimes;
}


