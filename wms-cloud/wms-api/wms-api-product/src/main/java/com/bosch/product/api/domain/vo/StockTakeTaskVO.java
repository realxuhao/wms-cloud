package com.bosch.product.api.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-04-18 09:58
 **/
@Data
public class StockTakeTaskVO {
    @ApiModelProperty(value = "任务号")
    private String taskNo;
    @ApiModelProperty(value = "下发时间")
    private String issusDate;
}
