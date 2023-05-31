package com.bosch.vehiclereservation.api.domain.vo;

import com.bosch.vehiclereservation.api.enumeration.ReserveStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class SupplierReserveDetailVO {

    /**
     * 采购id
     */
    @ApiModelProperty(value = "采购id")
    private Long purchaseId;

    /**
     * 供应商预约编号
     */
    @ApiModelProperty(value = "供应商预约编号")
    private String reserveNo;

    /**
     * 实际送货数量
     */
    @ApiModelProperty(value = "实际送货数量")
    private BigDecimal arriveQuantity;

    /**
     * 剩余送货数量
     */
    @ApiModelProperty(value = "剩余送货数量")
    private BigDecimal surplusQuantity;

    /**
     * 供应商编号
     */
    @ApiModelProperty(value = "供应商编号")
    private String supplierCode;

    /**
     * 供应商姓名
     */
    @ApiModelProperty(value = "供应商姓名")
    private String supplierName;

    /**
     * 状态：0：已预约 1：在途（司机已预约） 2：已到货（司机现场签到） 3：已完成
     */
    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "状态描述")
    private String statusDes;

    /**
     * 预约送货日期
     */
    @ApiModelProperty(value = "预约送货日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date reserveDate;

    /**
     * 预约送货--时间段
     */
    @ApiModelProperty(value = "时间段")
    private String timeWindow;

    /**
     * 签到时间
     */
    @ApiModelProperty(value = "签到时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date signinDate;


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
            case 4:
                this.statusDes = ReserveStatusEnum.ERROR.getDesc();
                break;
        }
    }
}
