package com.bosch.vehiclereservation.api.domain.vo;

import com.bosch.vehiclereservation.api.enumeration.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.annotation.Excel;
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
     * 预约类型：0：送货 1：取货
     */
    @ApiModelProperty(value = "预约类型描述")
    @Excel(name = "预约类型")
    private String driverTypeDes;

    /**
     * 车牌
     */
    @ApiModelProperty(value = "车牌")
    @Excel(name = "车牌号")
    private String carNum;

    /**
     * 是否预约： 0：未预约 1：已预约
     */
    @ApiModelProperty(value = "是否预约")
    @Excel(name = "是否预约")
    private String reserveTypeDes;

    /**
     * 预约时间
     */
    @ApiModelProperty(value = "预约时间")
    @Excel(name = "预约时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private String reserveDate;

    /**
     * 签到时间
     */
    @ApiModelProperty(value = "签到时间")
    @Excel(name = "签到时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date signinDate;
    /**
     * 是否迟到：0：未迟到 1：迟到
     */
    @ApiModelProperty(value = "是否迟到描述")
    @Excel(name = "是否迟到")
    private String lateDes;

    /**
     * 车间
     */
    @ApiModelProperty(value = "车间")
    @Excel(name = "Cell")
    private String cell;

    /**
     * 仓库编码
     */
    @ApiModelProperty(value = "仓库编码")
    @Excel(name = "仓库编码")
    private String wareCode;

    /**
     * 道口编号
     */
    @ApiModelProperty(value = "道口编号")
    @Excel(name = "道口")
    private String dockCode;

    /**
     * 状态：0：等待 1：进厂 2：完成
     */
    @ApiModelProperty(value = "状态描述")
    @Excel(name = "状态")
    private String statusDes;

    /**
     * 供应商编号
     */
    @ApiModelProperty(value = "供应商编号")
    @Excel(name = "供应商名称")
    private String supplierCode;

    /**
     * 司机姓名
     */
    @ApiModelProperty(value = "司机姓名")
    @Excel(name = "司机姓名")
    private String driverName;

    /**
     * 司机联系方式
     */
    @ApiModelProperty(value = "司机联系方式")
    @Excel(name = "司机联系方式")
    private String driverPhone;








    /**
     *
     */
    @ApiModelProperty(value = "仓库名称")
    private String wareName;

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
     * 供应商名称
     */
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

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
     * 司机签到状态：0：未签到 1：已签到
     */
    @ApiModelProperty(value = "司机签到状态")
    private Integer driverStatus;

    /**
     * 司机签到状态：0：未签到 1：已签到
     */
    @ApiModelProperty(value = "司机签到状态描述")
    private String driverStatusDes;



    @ApiModelProperty(value = "微信openid")
    private String wechatId;


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

    public void setStatus(Integer status) {
        this.status = status;
        if (status != null) {
            switch (status.intValue()) {
                case 0:
                    this.statusDes = DispatchStatusEnum.WAITE.getDesc();
                    break;
                case 1:
                    this.statusDes = DispatchStatusEnum.ENTER.getDesc();
                    break;
                case 2:
                    this.statusDes = DispatchStatusEnum.COMPLETE.getDesc();
                    break;
                case 3:
                    this.statusDes = DispatchStatusEnum.ERROR.getDesc();
                    break;
            }
        }
    }
}
