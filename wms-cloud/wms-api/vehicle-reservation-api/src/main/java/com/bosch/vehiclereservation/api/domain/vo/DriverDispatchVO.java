package com.bosch.vehiclereservation.api.domain.vo;

import com.bosch.vehiclereservation.api.enumeration.DispatchTypeEnum;
import com.bosch.vehiclereservation.api.enumeration.LateEnum;
import com.bosch.vehiclereservation.api.enumeration.ReserveTypeEnum;
import com.bosch.vehiclereservation.api.enumeration.SignStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class DriverDispatchVO {

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    private Long dispatchId;

    /**
     * 仓库id
     */
    @ApiModelProperty(value = "仓库id")
    private Long wareId;

    /**
     * 仓库名称
     */
    @ApiModelProperty(value = "仓库名称")
    private String wareName;

    /**
     * 道口编号
     */
    @ApiModelProperty(value = "道口编号")
    private String dockCode;

    /**
     * 订单类型：0：送货 1：取货
     */
    @ApiModelProperty(value = "订单类型：0：送货 1：取货")
    private Integer driverType;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sortNo;

    /**
     * 进厂时间
     */
    @ApiModelProperty(value = "进厂时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date comeinDate;

    /**
     * 完成时间
     */
    @ApiModelProperty(value = "完成时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date completeDate;

    /**
     * 状态：0：等待 1：进厂 2：完成
     */
    @ApiModelProperty(value = "状态：0：等待 1：进厂 2：完成")
    private Integer status;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 预约单号
     */
    @ApiModelProperty(value = "预约单号")
    private String reserveNo;

    /**
     * 供应商编号
     */
    @ApiModelProperty(value = "供应商编号")
    private String supplierCode;

    /**
     * 供应商名称
     */
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

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
    @ApiModelProperty(value = "预约类型")
    private Integer reserveType;

    /**
     * 是否迟到：0：未迟到 1：迟到
     */
    @ApiModelProperty(value = "是否迟到")
    private Integer late;

    /**
     * 签到时间
     */
    @ApiModelProperty(value = "签到时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date signinDate;

    /**
     * 司机签到状态：0：未签到 1：已签到
     */
    @ApiModelProperty(value = "司机签到状态")
    private Integer driverStatus;

    /**
     * 预约时间
     */
    @ApiModelProperty(value = "预约时间")
    private String reserveDate;

    /**
     * 车间
     */
    @ApiModelProperty(value = "车间")
    private String cell;


    /**
     * 订单类型：0：送货 1：取货
     */
    @ApiModelProperty(value = "订单类型描述")
    private String driverTypeDes;

    /**
     * 预约类型： 0：未预约 1：已预约
     */
    @ApiModelProperty(value = "预约类型描述")
    private String reserveTypeDes;

    /**
     * 是否迟到：0：未迟到 1：迟到
     */
    @ApiModelProperty(value = "是否迟到描述")
    private String lateDes;

    /**
     * 司机签到状态：0：未签到 1：已签到
     */
    @ApiModelProperty(value = "司机签到状态描述")
    private String driverStatusDes;


    public void setDriverType(Integer driverType) {
        this.driverType = driverType;
        if (driverType != null) {
            switch (driverType.intValue()) {
                case 0:
                    this.driverTypeDes = DispatchTypeEnum.DELIVER.getDesc();
                    break;
                case 1:
                    this.driverTypeDes = DispatchTypeEnum.PICKUP.getDesc();
                    break;
            }
        }
    }

    public void setReserveType(Integer reserveType) {
        this.reserveType = reserveType;
        if (reserveType != null) {
            switch (reserveType.intValue()) {
                case 0:
                    this.reserveTypeDes = ReserveTypeEnum.NOT_RESERVE.getDesc();
                    break;
                case 1:
                    this.reserveTypeDes = ReserveTypeEnum.RESERVED.getDesc();
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

    public void setDriverStatus(Integer driverStatus) {
        this.driverStatus = driverStatus;
        switch (driverStatus.intValue()) {
            case 0:
                this.driverStatusDes = SignStatusEnum.NOT_SIGN.getDesc();
                break;
            case 1:
                this.driverStatusDes = SignStatusEnum.SIGNED.getDesc();
                break;
        }
    }
}
