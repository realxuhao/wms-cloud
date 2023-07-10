package com.bosch.masterdata.api.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProInOutStockVO {

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
    @TableField(value = "operation_stock")
    private BigDecimal operationStock;

    /**
     * 操作类型
     */
    @TableField(value = "operation_type")
    private String operationType;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "create_time")
    private String createTime;


}
