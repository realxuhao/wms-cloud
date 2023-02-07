package com.bosch.binin.api.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-02-07 15:33
 **/
@Data
public class IQCSamplePlanQueryDTO {
    @ApiModelProperty(value = "sscc")
    private String ssccNb;
    @ApiModelProperty(value = "物料号")
    private String materialNb;
    @ApiModelProperty(value = "仓库号")
    private String wareCode;
    @ApiModelProperty(value = "状态")
    private List<Integer> statusList;
    @ApiModelProperty(value = "下架人")
    private String binDownUser;
    @ApiModelProperty(value = "开始下架时间")
    private Date startBinDownTime;
    @ApiModelProperty(value = "结束下架时间")
    private Date endBinDownTime;
    @ApiModelProperty(value = "抽样人")
    private String sampleUser;
    @ApiModelProperty(value = "开始抽样时间")
    private Date startSampleTime;
    @ApiModelProperty(value = "结束抽样时间")
    private Date endSampleTime;
}
