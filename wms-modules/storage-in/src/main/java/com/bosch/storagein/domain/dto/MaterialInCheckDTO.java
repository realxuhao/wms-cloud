package com.bosch.storagein.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MaterialInCheckDTO {

    @ApiModelProperty(value = "SSCC码")
    private String mesBarCode;

    @ApiModelProperty(value = "实际数量")
    private Integer actualQuantity;

    @ApiModelProperty(value = "实际称重、数数结果")
    private Double actualResult;
}
