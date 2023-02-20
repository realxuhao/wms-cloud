package com.bosch.vehiclereservation.api.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.web.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 车辆调度-司机签到时生成数据 vr_driver_pickup
 *
 * @author taojd
 * @date 2023-02-14
 */
@Data
@TableName("vr_driver_dispatch")
public class DriverDispatch extends BaseEntity {

    /**
     * 主键id
     */
    @TableId(value = "dispatch_id")
    private Long dispatchId;

    /**
     * vr_driver_deliver或者vr_driver_pickup的主键
     */
    private Long driverId;

    /**
     * 仓库id
     */
    private Long wareId;

    /**
     * 道口编号
     */
    private String dockCode;

    /**
     * 订单类型：0：送货 1：取货
     */
    private Integer driverType;

    /**
     * 排序
     */
    private Integer sortNo;

    /**
     * 进厂时间
     */
    private Date comeinDate;

    /**
     * 完成时间
     */
    private Date completeDate;

    /**
     * 状态：0：等待 1：进厂 2：完成
     */
    private Integer status;

}
