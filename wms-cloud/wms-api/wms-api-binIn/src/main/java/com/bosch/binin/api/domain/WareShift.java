package com.bosch.binin.api.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.web.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-11-16 20:43
 **/
@Data
@TableName("ware_shift")
public class WareShift extends BaseEntity {
    private static final long serialVersionUID = 1L;


    /**
     * 主键id
     */
    private Integer id;

    /**
     * 源工厂
     */
    private String sourcePlantNb;

    /**
     * 源仓库
     */
    private String sourceWareCode;

    /**
     * 源存储区
     */
    private String sourceAreaCode;

    /**
     * 源库位
     */
    private String sourceBinCode;

    /**
     * 移动类型
     */
    private String moveType;

    /**
     * sscc
     */
    private String ssccNb;

    /**
     * 物料号
     */
    private String materialNb;

    /**
     * 批次号
     */
    private String batchNb;

    /**
     * bbd过期时间
     */
    private Date expireDate;

    /**
     * 目的工厂
     */
    private String targetPlant;

    /**
     * 目的仓库
     */
    private String targetWareCode;

    /**
     * 状态：待下架（来自于②）、待发运（外库pda下架）、待收货（外库发运扫描）、已收货（目的仓库收货扫描）、已上架（pda扫描上架
     */
    private Integer state;

    /**
     * 删除标记1：删除，0:可用
     */
    private Integer deleteFlag;
}
