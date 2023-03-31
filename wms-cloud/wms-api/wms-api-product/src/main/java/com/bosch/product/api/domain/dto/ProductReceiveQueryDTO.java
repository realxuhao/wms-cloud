package com.bosch.product.api.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-03-29 13:31
 **/
@Data
public class ProductReceiveQueryDTO {

    @ApiModelProperty(value = "仓库")
    private String wareCode;

    @ApiModelProperty(value = "工厂")
    private String plantNb;

    @ApiModelProperty(value = "sscc")
    private String ssccNumber;

    @ApiModelProperty(value = "物料号")
    private String materialNb;

    @ApiModelProperty(value = "批次号")
    private String batchNb;

    @ApiModelProperty(value = "来源订单号")
    private String fromProdOrder;

    @ApiModelProperty(value = "状态，0：未入库，1：已入库")
    private Integer status;

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
