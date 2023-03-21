package com.bosch.product.api.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "ShippingHistoryVO")
public class ShippingHistoryVO {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "shippingTaskId")
    private Long shippingTaskId;

    @ApiModelProperty(value = "sscc码合集,用逗号隔开")
    private String ssccNumbers;

    @ApiModelProperty(value = "etoPo")
    private String etoPo;

    @ApiModelProperty(value = "prodOrder")
    private String prodOrder;

    @ApiModelProperty(value = "status")
    private Integer status;
}
