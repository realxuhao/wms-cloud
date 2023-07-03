package com.bosch.product.api.domain.dto;

import lombok.Data;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-07-02 15:53
 **/
@Data
public class EditBinDownQuantityDTO {
    private Long pickId;
    private Double newBinDownQuantity;
}
