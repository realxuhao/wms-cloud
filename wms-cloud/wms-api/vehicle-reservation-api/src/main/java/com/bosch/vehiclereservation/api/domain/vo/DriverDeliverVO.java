package com.bosch.vehiclereservation.api.domain.vo;

import com.bosch.vehiclereservation.api.enumeration.SignStatusEnum;
import com.bosch.vehiclereservation.api.enumeration.LateEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class DriverDeliverVO {

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
     * 供应商编号
     */
    @ApiModelProperty(value = "供应商编号")
    private String supplierCode;

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

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 状态描述：0：未签到 1：已签到
     */
    @ApiModelProperty(value = "状态描述")
    private String statusDes;

    /**
     * 迟到描述：0：未迟到1：迟到
     */
    @ApiModelProperty(value = "迟到描述")
    private String lateDes;

    public void setStatus(Integer status) {
        this.status = status;
        if (status != null) {
            switch (status.intValue()) {
                case 0:
                    this.statusDes = SignStatusEnum.NOT_SIGN.getDesc();
                    break;
                case 1:
                    this.statusDes = SignStatusEnum.SIGNED.getDesc();
                    break;
            }
        }
    }

    public void setLate(Integer late) {
        this.late = late;
        if (late != null) {
            switch (late.intValue()) {
                case 0:
                    this.lateDes = LateEnum.NOT_LATE.getDesc();
                    break;
                case 1:
                    this.lateDes = LateEnum.LATE.getDesc();
                    break;
            }
        }
    }
}
