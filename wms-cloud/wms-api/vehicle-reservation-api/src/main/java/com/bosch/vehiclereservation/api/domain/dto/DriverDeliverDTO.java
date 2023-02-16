package com.bosch.vehiclereservation.api.domain.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.core.web.page.PageDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class DriverDeliverDTO extends PageDomain {

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    private Long deliverId;

    /**
     * 供应商预约单号
     */
    @ApiModelProperty(value = "供应商预约单号")
    private String reserveNo;

    /**
     * 司机姓名
     */
    @ApiModelProperty(value = "司机姓名")
    private String driverName;

    /**
     * 司机联系方式
     */
    @ApiModelProperty(value = "司机联系方式")
    private String driverPhone;

    /**
     * 车牌
     */
    @ApiModelProperty(value = "车牌")
    private String carNum;

    /**
     * 预约类型： 0：未预约 1：已预约
     */
    @ApiModelProperty(value = "预约类型： 0：未预约 1：已预约")
    private Integer reserveType;

    /**
     * 是否迟到：0：未迟到1：迟到
     */
    @ApiModelProperty(value = "是否迟到：0：未迟到1：迟到")
    private Integer late;

    /**
     * 签到时间
     */
    @ApiModelProperty(value = "签到时间")
    private Date signinDate;

    /**
     * 状态：0：未签到 1：已签到
     */
    @ApiModelProperty(value = "状态：0：未签到 1：已签到")
    private Integer status;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

}