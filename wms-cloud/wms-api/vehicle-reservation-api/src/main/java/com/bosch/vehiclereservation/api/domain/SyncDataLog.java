package com.bosch.vehiclereservation.api.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.web.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 采购单同步日志 vr_syncdata_log
 *
 * @author taojd
 * @date 2023-03-01
 */
@Data
@TableName("vr_syncdata_log")
public class SyncDataLog extends BaseEntity {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 同步开始时间
     */
    private Date syncStartDate;

    /**
     * 同步结束时间
     */
    private Date syncEndDate;

    /**
     * 最后一条同步数据的时间
     */
    private Date syncLastDate;
}
