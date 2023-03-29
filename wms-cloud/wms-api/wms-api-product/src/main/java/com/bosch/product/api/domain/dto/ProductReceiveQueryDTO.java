package com.bosch.product.api.domain.dto;

import lombok.Data;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-03-29 13:31
 **/
@Data
public class ProductReceiveQueryDTO {

    private String wareCode;

    private String plantNb;

    private String ssccNb;

}
