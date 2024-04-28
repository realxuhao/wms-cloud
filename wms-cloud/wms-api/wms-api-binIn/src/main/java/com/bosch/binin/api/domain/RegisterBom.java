package com.bosch.binin.api.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.web.domain.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("register_bom")
public class RegisterBom extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 订单号
     */
    private String orderNumber;

    /**
     * 物料号
     */
    private String bomComponent;

    /**
     * 数量
     */
    private BigDecimal componentQuantity;

    /**
     * deleteFlag
     */
    private Integer deleteFlag;

}
