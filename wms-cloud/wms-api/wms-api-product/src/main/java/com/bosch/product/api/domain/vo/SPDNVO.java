package com.bosch.product.api.domain.vo;

import com.bosch.product.api.domain.SPDN;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-06-12 10:39
 **/
@Data
public class SPDNVO extends SPDN {

    /**
     * 物料名称
     */
    @ApiModelProperty(value = "物料名称")
    private String materialName;


    @ApiModelProperty(value = "箱TR对应的规格")
    private Double boxSpecification;


    @ApiModelProperty(value = "总数")
    private Double totalPCS;





}
