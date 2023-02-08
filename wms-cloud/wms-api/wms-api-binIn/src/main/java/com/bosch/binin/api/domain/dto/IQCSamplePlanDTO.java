package com.bosch.binin.api.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-02-08 10:16
 **/
@Data
public class IQCSamplePlanDTO {
    @NotNull
    @ApiModelProperty(value = "选中的新抽样sscc")
    private String ssccNb;
    @NotNull
    @ApiModelProperty(value = "抽样数量")
    private Double sampleQuantity;

    @ApiModelProperty(value = "源sscc,修改sscc时需要传")
    private String sourceSsccNb;
}
