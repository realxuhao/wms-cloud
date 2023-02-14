package com.bosch.vehiclereservation.api.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.web.domain.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * nutricia的采购订单 vr_purchase_order
 *
 * @author taojd
 * @date 2023-02-14
 */
@Data
@TableName("vr_purchase_order")
public class PurchaseOrder extends BaseEntity {

    /**
     * 主键id
     */
    @TableId(value = "purchase_id")
    private Long purchaseId;

    /**
     * 订单PO号
     */
    private String poCode;

    /**
     * 订单行号
     */
    private String poItem;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 料号
     */
    private String materialCode;

    /**
     * 物料名称
     */
    private String materialName;

    /**
     * 需求数量
     */
    private BigDecimal quantity;

    /**
     * 单位
     */
    private String unit;

    /**
     * 客户预计供应商到货日期
     */
    private Date deliveryDate;

    /**
     * 需求放行日期
     */
    private Date releaseDate;

    /**
     * 首批变更号
     */
    private String firstBatchChangeNo;

    /**
     * 海关台账号
     */
    private String cmsNumber;

    /**
     * 状态 0：正常 1：关闭
     */
    private Integer status;

}
