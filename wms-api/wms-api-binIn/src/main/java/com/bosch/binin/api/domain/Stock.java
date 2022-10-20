package com.bosch.binin.api.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.web.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * @author: UWH4SZH
 * @since: 10/18/2022 13:08
 * @description:　库存
 */
@Data
@TableName("bi_stock")
public class Stock extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * SSCC码
     */
    private String ssccNumber;
    /**
     * 仓库编码
     */
    private String wareCode;

    /**
     * 库位编码
     */
    private String binCode;

    /**
     * 物料号
     */
    private String materialNb;

    /**
     * 批次号
     */
    private String batchNb;

    /**
     * 过期时间
     */
    private Date expireDate;

    /**
     * 数量
     */
    private Integer quantity;


    /**
     * 上架id
     */
    private Long binInId;

    /**
     * 库存状态（0：不可用，1：可用）
     */
    private Integer status;


}
