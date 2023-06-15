package com.bosch.product.api.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
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

    private String externalRef;

    private String plant;

    private String deliveryNb;

    private String shipTo;

    private String shipDate;

    private String vendorCode;

    private String ssccNumber;

    private String deliveryItem;

    private String material;

    private String batch;

    private Double qty;


    private String uom;

    private String storageLocation;

    private String prodBatch;


    /**
     * 状态：
     */
    private Integer status;

    /**
     * 删除标记1：删除，0:可用
     */
    private Integer deleteFlag;
}
