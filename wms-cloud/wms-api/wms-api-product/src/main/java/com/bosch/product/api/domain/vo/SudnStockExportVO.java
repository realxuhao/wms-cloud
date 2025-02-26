package com.bosch.product.api.domain.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class SudnStockExportVO {

    @Excel(name = "fromProdOrder")
    private String fromProdOrder;

    @Excel(name = "仓库编码")
    private String wareCode;

    @Excel(name = "存储区编码")
    private String areaCode;

    @Excel(name = "SSCC")
    private String ssccNumber;

    @Excel(name = "Material")
    private String materialNb;

    @Excel(name = "物料名称")
    private String materialName;

    @Excel(name = "物料类型")
    private String materialType;

    @Excel(name = "库存量")
    private BigDecimal totalStock;

    @Excel(name = "批次库存量")
    private BigDecimal totalStockSum;

    @Excel(name = "冻结库存")
    private BigDecimal freezeStock;

    @Excel(name = "可用库存")
    private BigDecimal availableStock;

    @Excel(name = "箱Tr对应包装规格")
    private BigDecimal boxSpecification;

    @Excel(name = "库存量PCS")
    private BigDecimal pcsTotalStock;

    @Excel(name = "冻结库存PCS")
    private BigDecimal pcsFreezeStock;

    @Excel(name = "可用库存PCS")
    private BigDecimal pcsAvailableStock;

    @Excel(name = "质检状态")
    private String qualityStatus;

    @Excel(name = "unit")
    private String unit;

    @Excel(name = "cell")
    private String cell;

    @Excel(name = "Name of the ship-to party")
    private String nameOfShipToParty;

    @Excel(name = "Plant")
    private String plant;

    @Excel(name = "7761DeliveryNb")
    private String delivery;

    @Excel(name = "Ship-To")
    private String shipToParty;

    @Excel(name = "Ship Date")
    private String deliveryDate;

    @Excel(name = "Delivery_Item")
    private String item;

    @Excel(name = "Batch")
    private String batch;

    @Excel(name = "Qty")
    private BigDecimal deliveryQuantity;

    @Excel(name = "Uom")
    private String uom;

    @Excel(name = "StorageLocation")
    private String storageLocation;

    @Excel(name = "ProdBatch")
    private String productionBatch;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "出库时间",dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date binDownDate;

    @Excel(name = "7752DeliveryNb")
    private String deliveryNb;

}
