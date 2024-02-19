package com.bosch.product.api.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-05-25 15:54
 **/
@Data
public class ProductStockEditDTO {
    @ApiModelProperty("ssccNb")
    private String ssccNumber;
    @ApiModelProperty("总库存totalStock")
    private Double totalStock;
    @ApiModelProperty("可用库存")
    private Double availableStock;
    @ApiModelProperty("冻结库存")
    private Double freezeStock;
    /**
     * typeList: [{
     * 						text: '质检取样',
     * 						value: 0
     * 					                    },
     *                    {
     * 						text: '取样',
     * 						value: 1
     *                    },
     *                    {
     * 						text: '报废',
     * 						value: 2
     *                    },
     *                    {
     * 						text: '整托出库',
     * 						value: 3
     *                    },
     *                    {
     * 						text: '其它',
     * 						value: 4
     *                    },
     *                    {
     * 						text: '库存恢复',
     * 						value: 5
     *                    }
     * 				],
     */
    private Integer type;

    @ApiModelProperty("领料数量")
    private Double stockUse;



}
