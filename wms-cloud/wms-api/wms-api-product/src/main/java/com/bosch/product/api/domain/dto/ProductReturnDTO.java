package com.bosch.product.api.domain.dto;

import lombok.Data;

/**
 * @program: wms-cloud
 * @description: 成品退料
 * @author: xuhao
 * @create: 2023-06-27 16:51
 **/
@Data
public class ProductReturnDTO {


    private String qrCode;

    private String planNb;

    private Double quantity;

}
