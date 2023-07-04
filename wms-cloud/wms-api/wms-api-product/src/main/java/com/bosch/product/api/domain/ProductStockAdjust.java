package com.bosch.product.api.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-06-01 14:52
 **/
@Data
@TableName("product_stock_adjust")
public class ProductStockAdjust {
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
     * 工厂
     */
    private String plantNb;

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
     * 总库存
     */
    private Double totalStock;

    /**
     * 冻结库存
     */
    private Double freezeStock;

    /**
     * 可用库存
     */
    private Double availableStock;

    /**
     * 质检状态
     */
    private String qualityStatus;

    /**
     * 质检状态
     */
    private Integer deleteFlag;

    /**
     * 总库存
     */
    private Double adjustTotalStock;

    /**
     * 冻结库存
     */
    private Double adjustFreezeStock;

    /**
     * 可用库存
     */
    private Double adjustAvailableStock;

    /**
     * "调整类型,0:质检取样，1：取样，2：报废,3:整托出库,4:其他"
     */
    private Integer type;


}
