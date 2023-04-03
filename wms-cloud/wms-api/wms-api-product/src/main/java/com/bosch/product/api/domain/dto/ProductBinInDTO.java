package com.bosch.product.api.domain.dto;

import lombok.Data;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-03-30 14:35
 **/
@Data
public class ProductBinInDTO {

    private String recommendBinCode;
    private String binCode;
    private String sscc;
}
