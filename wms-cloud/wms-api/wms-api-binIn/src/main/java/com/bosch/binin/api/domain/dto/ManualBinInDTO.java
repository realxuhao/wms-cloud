package com.bosch.binin.api.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-12-21 16:53
 **/
@Data
public class ManualBinInDTO {

    @ApiModelProperty(value = "mesBarCode")
    private String mesBarCode;

    @ApiModelProperty(value = "实际库位/区域编码")
    private String actualCode;

    @ApiModelProperty(value = "实物托盘编码")
    private String palletCode;

    @ApiModelProperty(value = "0:上架到库位，1：上架到区域")
    private int type;


}
