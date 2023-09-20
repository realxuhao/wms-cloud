package com.bosch.product.api.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.web.domain.BaseEntity;
import lombok.Data;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-06-12 10:16
 **/
@TableName(value ="spdn")
@Data
public class SPDN extends BaseEntity {

    private Long id;

    @Excel(name = "externalRef")
    private String externalRef;
    @Excel(name = "plant")
    private String plant;
    @Excel(name = "deliveryNb")
    private String deliveryNb;
    @Excel(name = "shipTo")
    private String shipTo;
    @Excel(name = "shipDate")
    private String shipDate;
    @Excel(name = "vendorCode")
    private String vendorCode;
    @Excel(name = "ssccNumber")
    private String ssccNumber;
    @Excel(name = "deliveryItem")
    private String deliveryItem;
    @Excel(name = "material")
    private String material;
    @Excel(name = "batch")
    private String batch;
    @Excel(name = "qty(TR)",cellType = Excel.ColumnType.NUMERIC)
    private Double qty;

    @Excel(name = "uom")
    private String uom;
    @Excel(name = "storageLocation")
    private String storageLocation;
    @Excel(name = "prodBatch")
    private String prodBatch;


    /**
     * 状态：
     */
    @Excel(name = "status",readConverterExp = "0=已上传,1=已审批,2=已发运")
    private Integer status;

    /**
     * 删除标记1：删除，0:可用
     */
    private Integer deleteFlag;
}
