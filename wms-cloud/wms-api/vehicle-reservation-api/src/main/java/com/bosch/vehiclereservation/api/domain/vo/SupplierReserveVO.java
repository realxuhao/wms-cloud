package com.bosch.vehiclereservation.api.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SupplierReserveVO {

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    private Long reserveId;

    /**
     * 预约单号：20230301001
     */
    @ApiModelProperty(value = "预约单号")
    private String reserveNo;

    /**
     * 仓库id
     */
    @ApiModelProperty(value = "仓库id")
    private Long wareId;

    /**
     * 预约送货日期
     */
    @ApiModelProperty(value = "预约送货日期")
    private java.sql.Date reserveDate;

    /**
     * 时间段：09:00-10:00
     */
    @ApiModelProperty(value = "时间段")
    private String timeWindow;

    /**
     * 状态：0：已预约 1：在途（司机已预约） 2：已到货（司机现场签到） 3：已完成
     */
    @ApiModelProperty(value = "状态")
    private Integer status;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

}
