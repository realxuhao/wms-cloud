package com.bosch.binin.api.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-12-07 15:50
 **/
@Data
public class SplitPalletDTO {
    @ApiModelProperty(value = "源sscc")
    private String sourceSsccNb;
//    @ApiModelProperty(value = "拆托数量")
//    private Double splitQuantity;
    @ApiModelProperty(value = "新mesBarCode")
    private String newMesBarCode;
}
