package com.bosch.vehiclereservation.api.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.web.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 司机取货预约单对象 vr_driver_pickup
 *
 * @author taojd
 * @date 2023-02-14
 */
@Data
@TableName("vr_driver_pickup")
public class DriverPickup extends BaseEntity {

    /**
     * 主键id
     */
    @TableId(value = "pickup_id")
    private Long pickupId;

    /**
     * 微信id
     */
    private String wechatId;

    /**
     * 司机姓名
     */
    private String driverName;

    /**
     * 司机联系方式
     */
    private String driverPhone;

    /**
     * 车牌
     */
    private String carNum;

    /**
     * 车间：NMD ECN FSMP
     */
    private String cell;

    /**
     * 预约送货时间
     */
    private Date pickupDate;

    /**
     * 预约类型： 0：未预约 1：已预约
     */
    private Integer reserveType;

    /**
     * 是否迟到：0：未迟到1：迟到
     */
    private Integer late;

    /**
     * 签到时间
     */
    private Date signinDate;

    /**
     * 状态：0：未签到 1：已签到
     */
    private Integer status;

}
