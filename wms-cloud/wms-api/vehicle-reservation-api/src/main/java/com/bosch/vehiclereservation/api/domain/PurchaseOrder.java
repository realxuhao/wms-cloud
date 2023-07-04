package com.bosch.vehiclereservation.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
     * 物料系统ID
     */
    @TableField(value = "po_id")
    private String poId;

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

    /**
     * 预计到货开始日期
     */
    @TableField(exist = false)
    private Date startDate;

    /**
     * 预计到货结束日期
     */
    @TableField(exist = false)
    private Date endDate;

    @TableField(exist = false)
    private List<String> poNoList;

    @TableField(exist = false)
    private List<String> poItemList;

    @TableField(exist = false)
    private List<String> cmsNumberList;
    

}
