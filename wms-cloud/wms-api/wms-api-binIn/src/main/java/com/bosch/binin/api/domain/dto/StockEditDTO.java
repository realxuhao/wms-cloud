package com.bosch.binin.api.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-05-25 15:54
 **/
@Data
public class StockEditDTO {
    @ApiModelProperty("ssccNb")
    private String ssccNumber;
    @ApiModelProperty("总库存totalStock")
    private Double totalStock;
    @ApiModelProperty("可用库存")
    private Double availableStock;
    @ApiModelProperty("冻结库存")
    private Double freezeStock;
    @ApiModelProperty("调整类型,0:领料，1：报废，2：其他,3 配送到产线（玻璃瓶）")
    private Integer type;

    @ApiModelProperty("领料数量")
    private Double stockUse;



}
