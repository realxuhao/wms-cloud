package com.bosch.product.api.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-03-29 16:10
 **/
@Data
public class ProductStockQueryDTO {

    @ApiModelProperty(value = "工厂编码")
    private String plantNb;

    @ApiModelProperty(value = "sscc")
    private String ssccNumber;

    @ApiModelProperty(value = "仓库编码")
    private String wareCode;

    @ApiModelProperty(value = "区域编码")
    private String areCode;

    @ApiModelProperty(value = "库位编码")
    private String binCode;

    @ApiModelProperty(value = "来源订单号")
    private String fromProdOrder;

    @ApiModelProperty(value = "创建人")
    private String createBy;
    /**
     * 开始创建时间
     */
    @ApiModelProperty(value = "开始创建时间")
    private Date startCreateTime;

    /**
     * 结束创建时间
     */
    @ApiModelProperty(value = "结束创建时间")
    private Date endCreateTime;
}
