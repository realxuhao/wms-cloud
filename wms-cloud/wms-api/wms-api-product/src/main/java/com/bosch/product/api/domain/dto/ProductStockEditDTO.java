package com.bosch.product.api.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-05-25 15:54
 **/
@Data
public class ProductStockEditDTO {
    @ApiModelProperty("ssccNb")
    private String ssccNumber;
    @ApiModelProperty("总库存totalStock")
    private Double totalStock;
    @ApiModelProperty("可用库存")
    private Double availableStock;
    @ApiModelProperty("冻结库存")
    private Double freezeStock;
    @ApiModelProperty("调整类型,0:质检取样，1：取样，2：报废,3:整托出库,4:其他")
    private Integer type;

    @ApiModelProperty("领料数量")
    private Double stockUse;



}
