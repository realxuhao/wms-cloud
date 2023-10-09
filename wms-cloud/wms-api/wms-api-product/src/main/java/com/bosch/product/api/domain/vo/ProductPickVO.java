package com.bosch.product.api.domain.vo;

import com.bosch.product.api.domain.ProductPick;
import lombok.Data;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-06-27 11:05
 **/
@Data
public class ProductPickVO extends ProductPick {

    private String materialName;

    private String cell;
}
