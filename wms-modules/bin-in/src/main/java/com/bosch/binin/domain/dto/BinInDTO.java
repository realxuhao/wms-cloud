package com.bosch.binin.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: UWH4SZH
 * @since: 10/18/2022 14:06
 * @description:
 */
@Data
public class BinInDTO {

    @ApiModelProperty(value = "mesBarCode")
    private String mesBarCode;

    @ApiModelProperty(value = "实际托盘编码")
    private String actualBinCode;

}
