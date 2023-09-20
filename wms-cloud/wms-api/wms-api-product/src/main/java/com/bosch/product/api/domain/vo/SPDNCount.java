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
public class SPDNCount  {




    @ApiModelProperty(value = "总数")
    private Double totalPCS;

    @ApiModelProperty(value = "总数")
    private Double totalTR;





}
