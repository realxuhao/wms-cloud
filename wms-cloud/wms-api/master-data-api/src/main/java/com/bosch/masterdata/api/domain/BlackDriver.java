package com.bosch.masterdata.api.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.web.domain.BaseEntity;
import lombok.Data;

/**
 * 司机黑名单对象 md_black_driver
 *
 * @author taojd
 * @date 2023-02-13
 */
@Data
@TableName("md_black_driver")
public class BlackDriver extends BaseEntity {

    /**
     * 主键id
     */
    @TableId(value = "driver_id")
    private Long driverId;

    /**
     * 司机姓名
     */
    private String driverName;

    /**
     * 状态：1：启用黑名单 0：禁用黑名单
     */
    private Integer status;
}
