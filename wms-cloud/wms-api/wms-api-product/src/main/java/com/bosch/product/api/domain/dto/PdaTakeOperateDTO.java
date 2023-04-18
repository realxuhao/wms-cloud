package com.bosch.product.api.domain.dto;

import lombok.Data;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-04-18 11:03
 **/
@Data
public class PdaTakeOperateDTO {
    private Long detailId;
    private Double pdaTakeQuantity;
    private Boolean isDiff;


}
