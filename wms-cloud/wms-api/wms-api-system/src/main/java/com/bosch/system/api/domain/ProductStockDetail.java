package com.bosch.system.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.Data;

/**
 * 库存期初期末（job执行）
 * @TableName product_stock_detail
 */
@TableName(value ="product_stock_detail")
@Data
public class ProductStockDetail implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 工厂号
     */
    @TableField(value = "plant_nb")
    private String plantNb;

    /**
     * sscc码
     */
    @TableField(value = "sscc_number")
    private String ssccNumber;

    /**
     * 物料号
     */
    @TableField(value = "material_nb")
    private String materialNb;

    /**
     * 批次号
     */
    @TableField(value = "batch_nb")
    private String batchNb;

    /**
     * 总库存
     */
    @TableField(value = "total_stock")
    private BigDecimal totalStock;

    /**
     * 冻结库存
     */
    @TableField(value = "freeze_stock")
    private BigDecimal freezeStock;

    /**
     * 可用库存
     */
    @TableField(value = "available_stock")
    private BigDecimal availableStock;

    /**
     * 创建人
     */
    @TableField(value = "create_by")
    private String createBy;

    /**
     * 修改人
     */
    @TableField(value = "update_by")
    private String updateBy;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private  Date updateTime;

    /**
     * 状态（1：已执行，0：未执行）
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 删除标记1：删除，0:可用
     */
    @TableField(value = "delete_flag")
    private Integer deleteFlag;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}