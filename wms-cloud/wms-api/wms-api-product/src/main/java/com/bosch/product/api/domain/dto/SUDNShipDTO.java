package com.bosch.product.api.domain.dto;

import lombok.Data;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-07-02 13:04
 **/
@Data
public class SUDNShipDTO {
    private Long sudnId;
    private String carNb;
    private Double shipQuantity;
}
