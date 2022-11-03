package com.bosch.binin.api.domain.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-11-02 15:16
 **/
@Data
public class StockVO {

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
     * 物料名称
     */
    private String materialName;

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
    private Integer totalStock;

    /**
     * 冻结库存
     */
    private Integer freezeStock;

    /**
     * 可用库存
     */
    private Integer availableStock;


    /**
     * 上架id
     */
    private Long binInId;

    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    private Date createTime;

}
