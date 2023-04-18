package com.bosch.product.api.domain.vo;

import com.bosch.product.api.domain.StockTakeDetail;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-04-17 14:40
 **/
@Data
public class StockTakeDetailVO extends StockTakeDetail {
    @ApiModelProperty(value = "cell")
    private String cell;
    @ApiModelProperty(value = "循环物料类型")
    private int takeMaterialType;
    @ApiModelProperty(value = "盘点类型:0:明盘，1:盲盘(默认明盘)")
    private int type;
    @ApiModelProperty(value = "盘点方式,0:普通盘点，1：循环判断(默认普通盘点)")
    private int method;
    @ApiModelProperty(value = "物料名称")
    private String materialName;


}
