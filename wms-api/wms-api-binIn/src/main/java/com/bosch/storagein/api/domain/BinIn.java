package com.bosch.storagein.api.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author: UWH4SZH
 * @since: 10/18/2022 13:16
 * @description: 上架DO
 */
@Data
@TableName("bi_in")
public class BinIn {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

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
     * 仓库编码
     */
    private String wareCode;

    /**
     * 推荐库位编码
     */
    private String recommendBinCode;

    /**
     * 实际库位编码
     */
    private String actualBinCode;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 托盘编码
     */
    private String palletCode;

    /**
     * 托盘类型
     */
    private String palletType;

    /**
     * 状态(0：待入库,1:已入库)
     */
    private Integer state;

    /**
     * 创建人
     */
    private String createUser;


    /**
     * 创建时间
     */
    private Date creteTime;


}
