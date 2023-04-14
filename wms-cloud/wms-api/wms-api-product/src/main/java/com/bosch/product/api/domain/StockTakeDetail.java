package com.bosch.product.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 
 * @TableName stock_take_detail
 */
@TableName(value ="stock_take_detail")
@Data
public class StockTakeDetail implements Serializable {
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 计划编码
     */
    @TableField(value = "plan_code")
    private String planCode;

    /**
     * 物料编码
     */
    @TableField(value = "material_code")
    private String materialCode;

    /**
     * sscc
     */
    @TableField(value = "sscc_nb")
    private String ssccNb;

    /**
     * 批次号
     */
    @TableField(value = "batch_nb")
    private String batchNb;

    /**
     * 仓库编码
     */
    @TableField(value = "ware_code")
    private String wareCode;

    /**
     * 区域编码
     */
    @TableField(value = "area_code")
    private String areaCode;

    /**
     * 库位编码
     */
    @TableField(value = "bin_code")
    private String binCode;

    /**
     * 循环盘点月份
     */
    @TableField(value = "circle_take_month")
    private String circleTakeMonth;

    /**
     * 状态
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 库存数量
     */
    @TableField(value = "stock_quantity")
    private BigDecimal stockQuantity;

    /**
     * 盘点数量
     */
    @TableField(value = "take_quantity")
    private BigDecimal takeQuantity;

    /**
     * 0:无差异，1:有差异
     */
    @TableField(value = "is_diff")
    private Integer isDiff;

    /**
     * 任务编码
     */
    @TableField(value = "task_no")
    private String taskNo;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        StockTakeDetail other = (StockTakeDetail) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getPlanCode() == null ? other.getPlanCode() == null : this.getPlanCode().equals(other.getPlanCode()))
            && (this.getMaterialCode() == null ? other.getMaterialCode() == null : this.getMaterialCode().equals(other.getMaterialCode()))
            && (this.getSsccNb() == null ? other.getSsccNb() == null : this.getSsccNb().equals(other.getSsccNb()))
            && (this.getBatchNb() == null ? other.getBatchNb() == null : this.getBatchNb().equals(other.getBatchNb()))
            && (this.getWareCode() == null ? other.getWareCode() == null : this.getWareCode().equals(other.getWareCode()))
            && (this.getAreaCode() == null ? other.getAreaCode() == null : this.getAreaCode().equals(other.getAreaCode()))
            && (this.getBinCode() == null ? other.getBinCode() == null : this.getBinCode().equals(other.getBinCode()))
            && (this.getCircleTakeMonth() == null ? other.getCircleTakeMonth() == null : this.getCircleTakeMonth().equals(other.getCircleTakeMonth()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getStockQuantity() == null ? other.getStockQuantity() == null : this.getStockQuantity().equals(other.getStockQuantity()))
            && (this.getTakeQuantity() == null ? other.getTakeQuantity() == null : this.getTakeQuantity().equals(other.getTakeQuantity()))
            && (this.getIsDiff() == null ? other.getIsDiff() == null : this.getIsDiff().equals(other.getIsDiff()))
            && (this.getTaskNo() == null ? other.getTaskNo() == null : this.getTaskNo().equals(other.getTaskNo()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getPlanCode() == null) ? 0 : getPlanCode().hashCode());
        result = prime * result + ((getMaterialCode() == null) ? 0 : getMaterialCode().hashCode());
        result = prime * result + ((getSsccNb() == null) ? 0 : getSsccNb().hashCode());
        result = prime * result + ((getBatchNb() == null) ? 0 : getBatchNb().hashCode());
        result = prime * result + ((getWareCode() == null) ? 0 : getWareCode().hashCode());
        result = prime * result + ((getAreaCode() == null) ? 0 : getAreaCode().hashCode());
        result = prime * result + ((getBinCode() == null) ? 0 : getBinCode().hashCode());
        result = prime * result + ((getCircleTakeMonth() == null) ? 0 : getCircleTakeMonth().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getStockQuantity() == null) ? 0 : getStockQuantity().hashCode());
        result = prime * result + ((getTakeQuantity() == null) ? 0 : getTakeQuantity().hashCode());
        result = prime * result + ((getIsDiff() == null) ? 0 : getIsDiff().hashCode());
        result = prime * result + ((getTaskNo() == null) ? 0 : getTaskNo().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", planCode=").append(planCode);
        sb.append(", materialCode=").append(materialCode);
        sb.append(", ssccNb=").append(ssccNb);
        sb.append(", batchNb=").append(batchNb);
        sb.append(", wareCode=").append(wareCode);
        sb.append(", areaCode=").append(areaCode);
        sb.append(", binCode=").append(binCode);
        sb.append(", circleTakeMonth=").append(circleTakeMonth);
        sb.append(", status=").append(status);
        sb.append(", stockQuantity=").append(stockQuantity);
        sb.append(", takeQuantity=").append(takeQuantity);
        sb.append(", isDiff=").append(isDiff);
        sb.append(", taskNo=").append(taskNo);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}