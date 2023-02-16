package com.bosch.vehiclereservation.api.domain.vo;

import com.bosch.vehiclereservation.api.enumeration.ReserveStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

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
     * 供应商编号
     */
    @ApiModelProperty(value = "供应商编号")
    private String supplierCode;

    /**
     * 仓库id
     */
    @ApiModelProperty(value = "仓库id")
    private Long wareId;

    /**
     * 预约送货日期
     */
    @ApiModelProperty(value = "预约送货日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date reserveDate;

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

    @ApiModelProperty(value = "状态描述")
    private String statusDes;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "仓库名称")
    private String wareName;

    /**
     * 仓库地址
     */
    @ApiModelProperty(value = "仓库地址")
    private String wareLocation;

    /**
     * 仓库联系人
     */
    @ApiModelProperty(value = "仓库联系人")
    private String wareUser;

    /**
     * 仓库联系人电话
     */
    @ApiModelProperty(value = "仓库联系人电话")
    private String wareUserPhone;

    public void setStatus(Integer status) {
        this.status = status;
        switch (status) {
            case 0:
                this.statusDes = ReserveStatusEnum.RESERVED.getDesc();
                break;
            case 1:
                this.statusDes = ReserveStatusEnum.ON_ORDER.getDesc();
                break;
            case 2:
                this.statusDes = ReserveStatusEnum.ARRIVAL.getDesc();
                break;
            case 3:
                this.statusDes = ReserveStatusEnum.COMPLETE.getDesc();
                break;
        }
    }
}
