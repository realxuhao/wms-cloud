package com.bosch.binin.api.domain.dto;

import com.ruoyi.common.core.web.page.PageDomain;
import lombok.Data;

import java.util.Date;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-11-02 15:28
 **/
@Data
public class StockQueryDTO extends PageDomain {
    /**
     * SSCC码
     */
    private String ssccNumber;

    /**
     * 工厂
     */
    private String plantNb;


    /**
     * PO号
     */
    private String fromPurchaseOrder;


    /**
     * 仓库编码
     */
    private String wareCode;
    /**
     * 跨编码
     */
    private String frameCode;

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
    private Date startExpireDate;

    /**
     * 过期时间
     */
    private Date endExpireDate;
}
