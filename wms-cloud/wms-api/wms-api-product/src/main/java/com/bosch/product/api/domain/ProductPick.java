package com.bosch.product.api.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.web.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-06-26 16:59
 **/
@Data
@TableName(value ="product_pick")
public class ProductPick extends BaseEntity {
    private Long id;
    private String delivery;
    private String plant;
    private String shipToParty;
    private String nameOfShipToParty;
    private String deliveryDate;
    private String item;
    private String material;
    private String sscc;
    private String batch;
    private Date productionBatch;
    private Double deliveryQuantity;
    private String salesUnit;
    private Integer status;
    private String licensePlateNumber;
    private Date binDownDate;
    private String binDownPerson;
    private String shipper;
    private Date shippingTime;
    private Integer deleteFlag;
    private Date expireDate;
    /**
     * 仓库编码
     */
    private String wareCode;

    /**
     * 区域编码
     */
    private String areaCode;
    /**
     * 跨编码
     */
    private String frameCode;

    /**
     * 库位编码
     */
    private String binCode;

    private Long sudnId;


    private Double binDownQuantity;
}
