package com.bosch.product.api.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-03-31 16:14
 **/
@Data
public class ProductWareShiftQueryDTO {
    @ApiModelProperty(value = "源工厂")
    private String sourcePlantNb;
    @ApiModelProperty(value = "源仓库")
    private String sourceWareCode;
    @ApiModelProperty(value = "转运单号")
    private String orderNb;
    @ApiModelProperty(value = "目的仓库")
    private String targetWareCode;
    @ApiModelProperty(value = "转运单号")
    private Integer status;
    @ApiModelProperty(value = "车牌号")
    private String carNb;
    @ApiModelProperty(value = "sscc")
    private String ssccNb;
    @ApiModelProperty(value = "创建者")
    private String createBy;
    @ApiModelProperty(value = "开始创建时间")
    private String startCreateTime;
    @ApiModelProperty(value = "结束创建时间")
    private String endCreateTime;
    @ApiModelProperty(value = "开始更新时间")
    private String startUpdateTime;
    @ApiModelProperty(value = "结束更新时间")
    private String endUpdateTime;
    private String materialNb;
    private String batchNb;
    private String fromProdOrder;

    @ApiModelProperty(value = "ssccList")
    private List<String> ssccList;

}
