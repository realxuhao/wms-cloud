package com.bosch.system.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private LocalDateTime updateTime;

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
        ProductStockDetail other = (ProductStockDetail) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getPlantNb() == null ? other.getPlantNb() == null : this.getPlantNb().equals(other.getPlantNb()))
            && (this.getSsccNumber() == null ? other.getSsccNumber() == null : this.getSsccNumber().equals(other.getSsccNumber()))
            && (this.getMaterialNb() == null ? other.getMaterialNb() == null : this.getMaterialNb().equals(other.getMaterialNb()))
            && (this.getBatchNb() == null ? other.getBatchNb() == null : this.getBatchNb().equals(other.getBatchNb()))
            && (this.getTotalStock() == null ? other.getTotalStock() == null : this.getTotalStock().equals(other.getTotalStock()))
            && (this.getFreezeStock() == null ? other.getFreezeStock() == null : this.getFreezeStock().equals(other.getFreezeStock()))
            && (this.getAvailableStock() == null ? other.getAvailableStock() == null : this.getAvailableStock().equals(other.getAvailableStock()))
            && (this.getCreateBy() == null ? other.getCreateBy() == null : this.getCreateBy().equals(other.getCreateBy()))
            && (this.getUpdateBy() == null ? other.getUpdateBy() == null : this.getUpdateBy().equals(other.getUpdateBy()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getDeleteFlag() == null ? other.getDeleteFlag() == null : this.getDeleteFlag().equals(other.getDeleteFlag()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getPlantNb() == null) ? 0 : getPlantNb().hashCode());
        result = prime * result + ((getSsccNumber() == null) ? 0 : getSsccNumber().hashCode());
        result = prime * result + ((getMaterialNb() == null) ? 0 : getMaterialNb().hashCode());
        result = prime * result + ((getBatchNb() == null) ? 0 : getBatchNb().hashCode());
        result = prime * result + ((getTotalStock() == null) ? 0 : getTotalStock().hashCode());
        result = prime * result + ((getFreezeStock() == null) ? 0 : getFreezeStock().hashCode());
        result = prime * result + ((getAvailableStock() == null) ? 0 : getAvailableStock().hashCode());
        result = prime * result + ((getCreateBy() == null) ? 0 : getCreateBy().hashCode());
        result = prime * result + ((getUpdateBy() == null) ? 0 : getUpdateBy().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getDeleteFlag() == null) ? 0 : getDeleteFlag().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", plantNb=").append(plantNb);
        sb.append(", ssccNumber=").append(ssccNumber);
        sb.append(", materialNb=").append(materialNb);
        sb.append(", batchNb=").append(batchNb);
        sb.append(", totalStock=").append(totalStock);
        sb.append(", freezeStock=").append(freezeStock);
        sb.append(", availableStock=").append(availableStock);
        sb.append(", createBy=").append(createBy);
        sb.append(", updateBy=").append(updateBy);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", status=").append(status);
        sb.append(", deleteFlag=").append(deleteFlag);
        sb.append(", remark=").append(remark);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}