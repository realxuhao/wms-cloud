package com.bosch.binin.api.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-02-07 11:08
 **/
@Data
public class IQCSamplePlanVO {
    @ApiModelProperty(value = "sscc")
    private String ssccNb;
    @ApiModelProperty(value = "cell部门")
    private String cell;
    @ApiModelProperty(value = "物料号")
    private String materielNb;
    @ApiModelProperty(value = "批次号")
    private String batchNb;
    @ApiModelProperty(value = "BBD过期时间")
    private Date expireDate;
    @ApiModelProperty(value = "状态")
    private Integer status;
    @ApiModelProperty(value = "仓库编码")
    private String wareCode;
    @ApiModelProperty(value = "数量")
    private Double quantity;
    @ApiModelProperty(value = "单位")
    private String unit;
    @ApiModelProperty(value = "下架库位")
    private String binDownCode;
    @ApiModelProperty(value = "下架人")
    private String binDownUser;
    @ApiModelProperty(value = "下架时间")
    private Date binDownTime;
    @ApiModelProperty(value = "抽样数量")
    private Double sampleQuantity;
    @ApiModelProperty(value = "抽样人")
    private String sampleUser;
    @ApiModelProperty(value = "抽样时间")
    private Date sampleTime;
    @ApiModelProperty(value = "上架人")
    private String binInUser;
    @ApiModelProperty(value = "上架时间")
    private Date binInTime;
    @ApiModelProperty(value = "上架库位")
    private String binInCode;

}
