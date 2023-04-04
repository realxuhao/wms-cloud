package com.bosch.product.api.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-03-30 14:35
 **/
@Data
public class ProductBinInDTO {
    @ApiModelProperty(value = "推荐库位编码")
    private String recommendBinCode;
    @ApiModelProperty(value = "实际库位编码，和areaCode只传一个")
    private String binCode;
    @ApiModelProperty(value = "sscc")
    private String sscc;
    @ApiModelProperty(value = "区域编码。上架到区域。和binCode只传一个")
    private String areaCode;
}
