package com.bosch.vehiclereservation.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
    @TableId(value = "purchase_id", type = IdType.AUTO)
    private Long purchaseId;

    /**
     * 订单PO号
     */
    @TableField(value = "po_code")
    private String poNo;

    /**
     * 订单行号
     */
    private String poItem;

    /**
     * 供应商名称
     */
    @TableField(value = "supplier_name")
    private String supplier;

    /**
     * 料号
     */
    @TableField(value = "material_code")
    private String sapCode;

    /**
     * 物料名称
     */
    @TableField(value = "material_name")
    private String sapName;

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
