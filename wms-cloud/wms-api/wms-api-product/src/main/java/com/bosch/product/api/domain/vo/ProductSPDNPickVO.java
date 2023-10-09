package com.bosch.product.api.domain.vo;

import com.bosch.product.api.domain.ProductSPDNPick;
import lombok.Data;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-07-10 17:22
 **/
@Data
public class ProductSPDNPickVO extends ProductSPDNPick {

    private String materialName;

    private String cell;
}
