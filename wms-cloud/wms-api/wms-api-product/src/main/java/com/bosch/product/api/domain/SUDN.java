package com.bosch.product.api.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-06-26 15:22
 **/
@Data
@TableName(value ="sudn")
public class SUDN {
    private Long id;
    private String delivery;
    private String plant;
    private String shipToParty;
    private String nameOfShipToParty;
    private String deliveryDate;
    private String item;
    private String material;
    private Double deliveryQuantity;
    private String salesUnit;
    private int status;
}
