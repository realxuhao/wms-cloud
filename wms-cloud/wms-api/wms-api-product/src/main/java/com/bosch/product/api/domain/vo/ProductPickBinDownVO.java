package com.bosch.product.api.domain.vo;

import lombok.Data;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-06-27 15:23
 **/
@Data
public class ProductPickBinDownVO {
    private String sscc;
    private Double binDownQuality;
    //0:整，1：零
    private int type;
}
