package com.bosch.product.api.domain.dto;

import lombok.Data;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-04-20 16:13
 **/
@Data
public class EditStockDTO {
    private String barCode;
    private Double totalStock;
    private Double freezeStock;
}
