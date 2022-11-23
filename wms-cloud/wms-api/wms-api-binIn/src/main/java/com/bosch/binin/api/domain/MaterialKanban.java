package com.bosch.binin.api.domain;


import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.web.domain.BaseEntity;
import lombok.Data;

@Data
@TableName("material_kanban")
public class MaterialKanban extends BaseEntity {
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
     * 源工厂code
     */
    private String factoryCode;

    /**
     * 源工厂id
     */
    private Long factoryId;

    /**
     * 源仓库code
     */
    private String wareCode;
    /**
     * 源仓库id
     */
    private Long wareId;

    /**
     * 源存储区code
     */
    private String areaCode;

    /**
     * 源存储区id
     */
    private Long areaId;
    /**
     * 源库位code
     */
    private String binCode;
    /**
     * 源库位id
     */
    private Long binId;
    /**
     * 料号
     */
    private String materialCode;
    /**
     * 数量
     */
    private Double quantity;
    /**
     * 移动类型 （0： 收货，1：入库，2：上架，3：生产叫料）
     */
    private String moveType;
    /**
     * SSCC码
     */
    private String ssccNumber;
    /**
     * 需求cell
     */
    private String cell;
    /**
     * 动作类型（0：整托下架 1：拆托下架）
     */
    private Integer type;
    /**
     * 状态:状态：0:未执行，1:已执行
     * 状态:待下发（源工厂为7751的没有）、已下发、已下架（下架时PDA扫描SSCC）、配送完成（产线PDA扫描接收）
     */
    private Integer status;
    /**
     * deleteFlag
     */
    private Integer deleteFlag;
}
