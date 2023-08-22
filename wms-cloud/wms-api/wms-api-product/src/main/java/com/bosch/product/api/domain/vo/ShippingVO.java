package com.bosch.product.api.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "打包运输信息")
public class ShippingVO {

    @ApiModelProperty(value = "打包运输任务包裹编号")
    private String packageNo;

    @ApiModelProperty(value = "计划ID")
    private String shippingPlanId;

    @ApiModelProperty(value = "序号")
    private Integer historyIndex;

    @ApiModelProperty(value = "打包运输标记")
    private String shippingMark;

    @ApiModelProperty(value = "ETO_PO号")
    private String etoPo;

    @ApiModelProperty(value = "ETO工厂")
    private String etoPlant;

    @ApiModelProperty(value = "库存移动日期")
    private String stockMovementDate;

    @ApiModelProperty(value = "国家")
    private String country;

    @ApiModelProperty(value = "生产订单")
    private String prodOrder;

    @ApiModelProperty(value = "数量")
    private Integer qty;

    @ApiModelProperty(value = "是否拆包")
    private String isDisassembled;

    @ApiModelProperty(value = "TR")
    private String tr;

    @ApiModelProperty(value = "SAP编码")
    private String sapCode;

    @ApiModelProperty(value = "托盘数量")
    private String palletQuantity;

    @ApiModelProperty(value = "包装后")
    private String afterPacking;


    @ApiModelProperty(value = "打包运输历史ID")
    private Long shippingHistoryId;

    @ApiModelProperty(value = "SSCC号码")
    private String ssccNumbers;


    @ApiModelProperty(value = "任务创建者")
    private String taskCreateBy;

    @ApiModelProperty(value = "任务创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date taskCreateTime;

    @ApiModelProperty(value = "任务更新者")
    private String taskUpdateBy;

    @ApiModelProperty(value = "任务更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date taskUpdateTime;

    @ApiModelProperty(value = "打包创建者")
    private String historyCreateBy;

    @ApiModelProperty(value = "打包创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date historyCreateTime;

    @ApiModelProperty(value = "打包更新者")
    private String historyUpdateBy;

    @ApiModelProperty(value = "打包更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date historyUpdateTime;

    @ApiModelProperty(value = "prodSscc json")
    private String prodSscc;
}
