package com.bosch.product.api.domain.dto;

import com.ruoyi.common.core.web.page.PageDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: wms-cloud
 * @description: 成品退料
 * @author: xuhao
 * @create: 2023-06-27 16:51
 **/
@Data
public class ProductReturnDTO extends PageDomain {


    private String qrCode;

    private String plantNb;

    private Double quantity;

    private String productNb;

    @ApiModelProperty(value = "0：经销商退货，1：退货到工厂")
    private Integer type;

    private String ssccNumber;

    private String wareCode;

}
