package com.bosch.product.api.domain.dto;

import com.ruoyi.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "ShippingHistoryDTO")
public class ShippingHistoryDTO  extends BaseEntity {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "序号")
    private Integer historyIndex;

    @ApiModelProperty(value = "shippingTaskId")
    private Long shippingTaskId;

    @ApiModelProperty(value = "etoPo")
    private String etoPo;

    @ApiModelProperty(value = "prodOrder")
    private String prodOrder;

    @ApiModelProperty(value = "sscc码合集,用逗号隔开")
    private String ssccNumbers;

    @ApiModelProperty(value = "prodSscc json")
    private String prodSscc;

    @ApiModelProperty(value = "status")
    private Integer status;

    @ApiModelProperty(value = "deleteFlag")
    private Integer deleteFlag;

    @ApiModelProperty(value = "是否是最后一托 0：否 1：是")
    private Integer lastOne;
}
