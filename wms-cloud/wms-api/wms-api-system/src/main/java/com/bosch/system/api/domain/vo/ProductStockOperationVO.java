package com.bosch.system.api.domain.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 库存操作记录表
 * @TableName product_stock_operation
 */
@Data
public class ProductStockOperationVO  {
    /**
     * 
     */

    private Long id;

    /**
     * 工厂号
     */
    @ApiModelProperty(value = "plant_nb")
    private String plantNb;

    /**
     * sscc码
     */
    @ApiModelProperty(value = "sscc_number")
    private String ssccNumber;

    /**
     * 物料号
     */
    @ApiModelProperty(value = "material_nb")
    private String materialNb;

    /**
     * 批次号
     */
    @ApiModelProperty(value = "batch_nb")
    private String batchNb;

    /**
     * 操作库存
     */
    @ApiModelProperty(value = "operation_stock")
    private BigDecimal operationStock;

    /**
     * 操作类型
     */
    @ApiModelProperty(value = "operation_type")
    private String operationType;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "create_by")
    private String createBy;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "update_by")
    private String updateBy;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "update_time")
    private LocalDateTime updateTime;

    /**
     * 状态（1：已执行，0：未执行）
     */
    @ApiModelProperty(value = "status")
    private Integer status;

    /**
     * 删除标记1：删除，0:可用
     */
    @ApiModelProperty(value = "delete_flag")
    private Integer deleteFlag;

    /**
     * 备注
     */
    @ApiModelProperty(value = "remark")
    private String remark;


}