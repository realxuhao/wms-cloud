package com.bosch.vehiclereservation.api.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.web.domain.BaseEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 司机送货预约单对象 vr_driver_deliver
 *
 * @author taojd
 * @date 2023-02-13
 */
@Data
@TableName("vr_driver_deliver")
public class DriverDeliver extends BaseEntity {

    /**
     * 主键id
     */
    @TableId(value = "deliver_id", type = IdType.AUTO)
    private Long deliverId;

    /**
     * 供应商预约单号
     */
    private String reserveNo;

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
     * 预约类型： 0：未预约 1：已预约
     */
    private Integer reserveType;

    /**
     * 是否迟到：0：未迟到 1：迟到
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


    @TableField(exist = false)
    private List<String> reserveNoList;

    @TableField(exist = false)
    private String supplierCode;

    /**
     * 预约送货日期
     */
    @TableField(exist = false)
    private Date reserveDate;

    /**
     * 时间段：09:00-10:00
     */
    @TableField(exist = false)
    private String timeWindow;

    /**
     * 查询类型：0:不执行按照签到时间排序规则 1:按照签到时间排序
     */
    @TableField(exist = false)
    private Integer selectType;
}
