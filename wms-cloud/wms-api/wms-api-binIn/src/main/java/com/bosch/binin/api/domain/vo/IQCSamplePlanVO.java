package com.bosch.binin.api.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.annotation.Excel;
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
    private Long id;
    @ApiModelProperty(value = "sscc")
    @Excel(name = "sscc")
    private String ssccNb;
    @ApiModelProperty(value = "cell部门")
    @Excel(name = "cell部门")
    private String cell;
    @ApiModelProperty(value = "物料号")
    @Excel(name = "物料号")
    private String materialNb;
    @ApiModelProperty(value = "物料名称")
    @Excel(name = "物料名称")
    private String materialName;
    @ApiModelProperty(value = "批次号")
    @Excel(name = "批次号")
    private String batchNb;
    @ApiModelProperty(value = "BBD过期时间")
    @Excel(name = "BBD过期时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expireDate;
    @ApiModelProperty(value = "状态")
    @Excel(name = "状态")
    private Integer status;
    @ApiModelProperty(value = "仓库编码")
    @Excel(name = "仓库编码")
    private String wareCode;
    @ApiModelProperty(value = "数量")
    @Excel(name = "数量")
    private Double quantity;
    @ApiModelProperty(value = "单位")
    @Excel(name = "单位")
    private String unit;
    @ApiModelProperty(value = "下架库位")
    @Excel(name = "下架库位")
    private String binDownCode;
    @ApiModelProperty(value = "下架人")
    @Excel(name = "下架人")
    private String binDownUser;
    @ApiModelProperty(value = "下架时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "下架时间")
    private Date binDownTime;
    @ApiModelProperty(value = "抽样数量")
    @Excel(name = "抽样数量")
    private Double sampleQuantity;
    @ApiModelProperty(value = "抽样人")
    @Excel(name = "抽样人")
    private String sampleUser;
    @ApiModelProperty(value = "抽样时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "抽样时间")
    private Date sampleTime;
    @ApiModelProperty(value = "上架人")
    @Excel(name = "上架人")
    private String binInUser;
    @ApiModelProperty(value = "上架时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "上架时间")
    private Date binInTime;
    @ApiModelProperty(value = "上架库位")
    @Excel(name = "上架库位")
    private String binInCode;
    @ApiModelProperty(value = "推荐抽样数量")
    @Excel(name = "推荐抽样数量")
    private Double recommendSampleQuantity;
    private String plantNb;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
