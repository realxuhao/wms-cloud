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

    @ApiModelProperty(value = "物料编码")
    private String materialNb;

    @ApiModelProperty(value = "物料类型")
    private String materialType;

    @ApiModelProperty(value = "批次号")
    private String batchNb;

    @ApiModelProperty(value = "仓库编码")
    private String wareCode;

    @ApiModelProperty(value = "区域编码")
    private String areaCode;

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

    @ApiModelProperty(value = "质检状态")
    private String qualityStatus;

    /**
     * 过期时间
     */
    @ApiModelProperty(value = "过期开始时间")
    private Date startExpireDate;

    /**
     * 过期时间
     */
    @ApiModelProperty(value = "过期结束时间")
    private Date endExpireDate;


    private String cell;
}
