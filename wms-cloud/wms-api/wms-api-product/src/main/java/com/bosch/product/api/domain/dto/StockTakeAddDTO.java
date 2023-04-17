package com.bosch.product.api.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-04-17 10:16
 **/
@Data
public class StockTakeAddDTO {
    @ApiModelProperty(value = "cell")
    private String cell;
    @ApiModelProperty(value = "仓库编码")
    private String wareCode;
    @ApiModelProperty(value = "区域编码")
    private String areaCode;
    @ApiModelProperty(value = "盘点类型:0:明盘，1:盲盘(默认明盘)")
    private int type;
    @ApiModelProperty(value = "盘点方式,0:普通盘点，1：循环判断(默认普通盘点)")
    private int method;
    @ApiModelProperty(value = "循环盘点月份")
    private Integer circleTakeMonth;
    @ApiModelProperty(value = "循环物料类型")
    private int takeMaterialType;


}
