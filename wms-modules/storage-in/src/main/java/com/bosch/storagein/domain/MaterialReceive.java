package com.bosch.storagein.domain;

import lombok.Data;

import java.util.Date;

/**
 * 原材料收货对象 md_material_receive
 *
 * @author xuhao
 * @date 2022-09-29
 */

@Data
public class MaterialReceive {

    /**
     * id
     */
    private Long id;

    /**
     * 仓库
     */
    private String plantNb;

    /**
     * SSCC码
     */
    private String ssccNumber;

    /**
     * 物料号
     */
    private String materialNb;

    /**
     * 批次号
     */
    private String batchNb;

    /**
     * 供应商
     */
    private String supplierNb;

    /**
     * 存储区域
     */
    private String storageLocation;

    /**
     * BBD(过期时间)
     */
    private String expireDate;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 单位
     */
    private String unit;

    /**
     * 来源区域
     */
    private String fromArea;

    /**
     * 来源PO号
     */
    private String fromPurchaseOrder;

    /**
     * 木器分拣区域
     */
    private String toPickingArea;

    /**
     * PO行号
     */
    private String poNumberItem;

    /**
     * 上传人
     */
    private String upload_user;

    /**
     * 上传时间
     */
    private Date upload_time;

    /**
     * 更新人
     */
    private String update_user;

    /**
     * 更新时间
     */
    private Date update_time;

    /**
     * 删除标记1:可用，0:删除
     */
    private Integer deleteFlag;

    /**
     * 物料流转状态:0:待入库，1：已入库
     */
    private Integer status;

    /**
     * 关联的fileId
     */
    private Integer fileId;
}
