package com.bosch.vehiclereservation.api.domain.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class RecordDTO {

    /**
     * 物料系统ID
     */
    private String poId;

    /**
     * 物料系统ID
     */
    private String id;

    /**
     * 订单号
     */
    private String poNo;

    /**
     * 订单行号
     */
    private String poItem;

    /**
     * 供应商名称
     */
    private String supplier;

    /**
     * 物料代号
     */
    private String sapCode;

    /**
     * 物料名称
     */
    private String sapName;

    /**
     * 数量
     */
    private BigDecimal quantity;

    /**
     * 单位
     */
    private String unit;

    /**
     * 预计收到日期
     */
    //@JsonFormat(pattern = "yyyy-MM-dd")
    private Date deliveryDate;

    /**
     * 需求放行日期
     */
    //@JsonFormat(pattern = "yyyy-MM-dd")
    private Date releaseDate;

    /**
     * 生产商名称
     */
    private String manufacture;

    /**
     * 海关台账号
     */
    private String cmsNumber;

    /**
     * 创建日期
     */
    //@JsonFormat(pattern = "yyyy-MM-dd")
    private Date createDate;

    /**
     * 更新日期
     */
    //@JsonFormat(pattern = "yyyy-MM-dd")
    private String updateDate;

    /**
     * 备注
     */
    private String remark;

    /**
     * 发票号
     */
    private String invoiceNo;

    /**
     * 0： 正常， 1：删除
     */
    private Integer deleteFlag;

    /**
     * 首批变更号
     */
    private String firstBatchChangeNo;

    /**
     * 1:ecn, 2: fsmp,3:nmd
     */
    private Integer type;


    public void setId(String id) {
        this.id = id;
        this.poId = id;
    }
}
