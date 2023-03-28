package com.bosch.binin.api.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-03-27 16:11
 **/
@Data
public class MaterialReturnConfirmDTO {

    @ApiModelProperty(value = "sscc")
    private List<String> ssccNumbers;
    @ApiModelProperty(value = "仓库")
    private String wareCode;
    @ApiModelProperty(value = "区域")
    private String areaCode;
    @ApiModelProperty(value = "工厂")
    private String plantNb;
}
