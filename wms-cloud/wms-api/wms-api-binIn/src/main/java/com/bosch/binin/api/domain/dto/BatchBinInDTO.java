package com.bosch.binin.api.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-05-22 09:46
 **/
@Data
public class BatchBinInDTO {
    @ApiModelProperty(value = "按批时候传递")
    private String mesBarCode;
    @ApiModelProperty(value = "多托时候传递")
    private List<String> mesBarCodes;
    @ApiModelProperty(value = "0:按批，1：多托")
    private int type;
    @ApiModelProperty(value = "区域编码")
    private String areaCode;
}
