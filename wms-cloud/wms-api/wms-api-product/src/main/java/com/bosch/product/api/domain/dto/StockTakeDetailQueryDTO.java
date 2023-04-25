package com.bosch.product.api.domain.dto;

import com.ruoyi.common.core.web.page.PageDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-04-17 14:12
 **/
@Data
public class StockTakeDetailQueryDTO extends PageDomain {

    @ApiModelProperty(value = "ids")
    private List<Long> ids;
    @ApiModelProperty(value = "物料号")
    private String materialCode;
    @ApiModelProperty(value = "批次号")
    private String batchNb;
    @ApiModelProperty(value = "cell")
    private String cell;
    @ApiModelProperty(value = "仓库编码")
    private String wareCode;
    @ApiModelProperty(value = "区域编码")
    private String areaCode;
    @ApiModelProperty(value = "盘点类型:0:明盘，1:盲盘(默认明盘)")
    private Integer type;
    @ApiModelProperty(value = "盘点方式,0:普通盘点，1：循环判断(默认普通盘点)")
    private Integer method;
    @ApiModelProperty(value = "循环盘点月份,循环盘点时需要传")
    private Integer circleTakeMonth;
    @ApiModelProperty(value = "计划号")
    private String planCode;
    @ApiModelProperty(value = "循环物料类型")
    private Integer takeMaterialType;
    @ApiModelProperty(value = "详情状态")
    private Integer status;
    @ApiModelProperty(value = "下发类型，0：普通盘点，1：循环盘点")
    private int issueType;
    @ApiModelProperty(value = "任务号")
    private String taskNo;
    @ApiModelProperty(value = "开始创建时间")
    private Date startCreateTime;
    @ApiModelProperty(value = "结束创建时间")
    private Date endCreateTime;
    @ApiModelProperty(value = "开始创建时间")
    private Date startIssueTime;
    @ApiModelProperty(value = "结束创建时间")
    private Date endIssueTime;
    @ApiModelProperty(value = "ssccNb")
    private String ssccNb;

}
