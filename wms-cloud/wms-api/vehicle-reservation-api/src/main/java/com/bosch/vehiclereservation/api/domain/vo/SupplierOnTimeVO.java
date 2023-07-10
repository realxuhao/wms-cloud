package com.bosch.vehiclereservation.api.domain.vo;

import com.ruoyi.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SupplierOnTimeVO {

    /**
     * 供应商名称
     */
    @Excel(name = "供应商名称")
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    /**
     * 年份
     */
    @Excel(name = "年份")
    @ApiModelProperty(value = "年份")
    private Integer yearOn;

    /**
     * 月份
     */
    @Excel(name = "月份")
    @ApiModelProperty(value = "月份")
    private Integer monthOn;

    /**
     * 签到总数
     */
    @Excel(name = "签到总数")
    @ApiModelProperty(value = "签到总数")
    private Integer totalCount;

    /**
     * 迟到总数
     */
    @Excel(name = "迟到总数")
    @ApiModelProperty(value = "迟到总数")
    private Integer lateCount;

    /**
     * 准时率
     */
    @Excel(name = "准时率")
    @ApiModelProperty(value = "准时率")
    private String onTimeRatio;



}
