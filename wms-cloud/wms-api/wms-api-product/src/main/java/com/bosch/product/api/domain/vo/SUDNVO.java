package com.bosch.product.api.domain.vo;

import com.bosch.product.api.domain.SUDN;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-06-26 15:50
 **/
@Data
public class SUDNVO extends SUDN {
    /**
     * 物料名称
     */
    @ApiModelProperty(value = "物料名称")
    private String materialName;

    private Set<String> productionBatchs;

    private String cell;


}
