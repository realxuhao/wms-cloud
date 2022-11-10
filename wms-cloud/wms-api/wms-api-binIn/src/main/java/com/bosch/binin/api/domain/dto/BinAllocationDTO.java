package com.bosch.binin.api.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: UWH4SZH
 * @since: 10/18/2022 13:28
 * @description:
 */
@Data
public class BinAllocationDTO {

    @ApiModelProperty(value = "mesBarCode")
    private String mesBarCode;

    @ApiModelProperty(value = "托盘类型")
    private String palletType;

    @ApiModelProperty(value = "托盘编码")
    private String palletCode;


}
