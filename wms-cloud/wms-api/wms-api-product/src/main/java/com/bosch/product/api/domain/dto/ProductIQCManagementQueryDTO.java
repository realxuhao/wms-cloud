package com.bosch.product.api.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-03-31 17:15
 **/
@Data
public class ProductIQCManagementQueryDTO extends ProductStockQueryDTO {

    /**
     * 已质检状态
     */
    @ApiModelProperty(value = "已质检状态")
    private String changeStatus;

}
