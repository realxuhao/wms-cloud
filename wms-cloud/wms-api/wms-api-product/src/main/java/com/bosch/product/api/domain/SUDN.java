package com.bosch.product.api.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.web.domain.BaseEntity;
import lombok.Data;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-06-26 15:22
 **/
@Data
@TableName(value ="sudn")
public class SUDN extends BaseEntity {
    private Long id;
    private String delivery;
    private String plant;
    private String shipToParty;
    private String nameOfShipToParty;
    private String deliveryDate;
    private String item;
    private String material;
    private Double deliveryQuantity;
    private String batch;
    private String salesUnit;
    private int status;
    private Double shipQuantity;
    private String carNb;
    private double sumBinDownQuantity;

    /**
     * 删除标记1：删除，0:可用
     */
    @TableField(value = "delete_flag")
    private Integer deleteFlag;

}
