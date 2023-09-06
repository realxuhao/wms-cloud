package com.bosch.vehiclereservation.api.domain.vo;

import com.bosch.vehiclereservation.api.enumeration.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class DriverDispatchNotSignVO {

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
     * 预约单号
     */
    @ApiModelProperty(value = "预约单号")
    @Excel(name = "供应商预约单号")
    private String reserveNo;

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
     * 车牌
     */
    @ApiModelProperty(value = "车牌")
    @Excel(name = "车牌号")
    private String carNum;

    /**
     * 订单类型：0：送货 1：取货
     */
    @ApiModelProperty(value = "订单类型：0：送货 1：取货")
    private Integer driverType;

    /**
     * 预约类型： 0：未预约 1：已预约
     */
    @ApiModelProperty(value = "预约类型")
    private Integer reserveType;





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

}
