package com.bosch.binin.api.domain.dto;

import com.ruoyi.common.core.web.page.PageDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class MaterialReturnDTO {

    @ApiModelProperty(value = "mesBarCode")
    private String mesBarCode;

    @ApiModelProperty(value = "数量")
    private Double quantity;

    @ApiModelProperty(value = "部门cell")
    private String cell;


    @ApiModelProperty(value = "sscc")
    private String ssccNb;

}
