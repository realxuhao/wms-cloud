package com.bosch.vehiclereservation.api.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.web.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 供应商预约单 vr_supplier_reserve
 *
 * @author taojd
 * @date 2023-02-14
 */
@Data
@TableName("vr_supplier_reserve")
public class SupplierReserve extends BaseEntity {

    /**
     * 主键id
     */
    @TableId(value = "reserve_id", type = IdType.AUTO)
    private Long reserveId;

    /**
     * 预约单号：20230301001
     */
    private String reserveNo;

    /**
     * 供应商编号
     */
    private String supplierCode;

    /**
     * 仓库id
     */
    private Long wareId;

    /**
     * 预约送货日期
     */
    private Date reserveDate;

    /**
     * 时间段
     */
    @TableField(exist = false)
    private String timeWindow;

    /**
     * 状态：0：已预约 1：在途（司机已预约） 2：已到货（司机现场签到） 3：已完成
     */
    private Integer status;

}
