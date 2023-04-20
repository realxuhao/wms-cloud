package com.bosch.product.api.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-04-18 11:03
 **/
@Data
public class PdaTakeOperateDTO {
    @ApiModelProperty("detailID")
    private Long detailId;
    @ApiModelProperty("盘点数量")
    private Double pdaTakeQuantity;
    @ApiModelProperty("是否一致")
    private Boolean isDiff;



}
