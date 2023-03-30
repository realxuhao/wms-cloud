package com.bosch.product.api.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-03-29 15:02
 **/
@Data
@TableName("stock_product")
public class ProductStock {
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
     * PO号
     */
    private String fromProdOrder;


    /**
     * 质检状态
     */
    private String qualityStatus;

    /**
     * 质检状态
     */
    private Integer deleteFlag;


    private String palletCode;

    /**
     * 整托标记，0：整托，1：零托
     */
    private int wholeFlag;

    /**
     * 变更状态（0：未变更，1：已变更）
     */
    private int changeStatus;

    private String recommendBinCode;

    /**
     * 生产批次号
     */

    private Date productionDate;


    private String unit;


    /**
     * 上架标识，上架标识，0:不需要上架，1:待上架，2：已上架
     */
    private Integer binInFlag;
}
