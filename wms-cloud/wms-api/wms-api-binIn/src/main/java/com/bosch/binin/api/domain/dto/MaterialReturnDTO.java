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

    @ApiModelProperty(value = "工厂")
    private String plantNb;

    @ApiModelProperty(value = "仓库")
    private String wareCode;

    @ApiModelProperty(value = "区域")
    private String areaCode;

    @ApiModelProperty(value = "数量")
    private Double quantity;

    @ApiModelProperty(value = "0：正常退库,1：异常退库")
    private Integer type;

    @ApiModelProperty(value = "部门cell")
    private String cell;

}
