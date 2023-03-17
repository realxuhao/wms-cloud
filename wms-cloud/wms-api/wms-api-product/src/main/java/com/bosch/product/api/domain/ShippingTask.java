package com.bosch.product.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName shipping_task
 */
@TableName(value ="shipping_task")
@Data
public class ShippingTask implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    private String packageNo;
    /**
     * plan表id
     */
    private Long shippingPlanId;

    /**
     * 
     */
    private String shippingMark;

    /**
     * 
     */
    private String etoPo;

    /**
     * 
     */
    private String etoPlant;

    /**
     * 
     */
    private String stockMovementDate;

    /**
     * 
     */
    private String country;

    /**
     * 
     */
    private String prodOrder;

    /**
     * 
     */
    private Integer qty;

    /**
     * 
     */
    private String isDisassembled;

    /**
     * 
     */
    private String tr;

    /**
     * 
     */
    private String sapCode;

    /**
     * 
     */
    private String palletQuantity;

    /**
     * 
     */
    private String afterPacking;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 修改人
     */
    private String updateBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 状态（1：已执行，0：未执行）
     */
    private Integer status;

    /**
     * 删除标记1：删除，0:可用
     */
    private Integer deleteFlag;

    /**
     * 备注
     */
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
        ShippingTask other = (ShippingTask) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getShippingPlanId() == null ? other.getShippingPlanId() == null : this.getShippingPlanId().equals(other.getShippingPlanId()))
            && (this.getShippingMark() == null ? other.getShippingMark() == null : this.getShippingMark().equals(other.getShippingMark()))
            && (this.getEtoPo() == null ? other.getEtoPo() == null : this.getEtoPo().equals(other.getEtoPo()))
            && (this.getEtoPlant() == null ? other.getEtoPlant() == null : this.getEtoPlant().equals(other.getEtoPlant()))
            && (this.getStockMovementDate() == null ? other.getStockMovementDate() == null : this.getStockMovementDate().equals(other.getStockMovementDate()))
            && (this.getCountry() == null ? other.getCountry() == null : this.getCountry().equals(other.getCountry()))
            && (this.getProdOrder() == null ? other.getProdOrder() == null : this.getProdOrder().equals(other.getProdOrder()))
            && (this.getQty() == null ? other.getQty() == null : this.getQty().equals(other.getQty()))
            && (this.getIsDisassembled() == null ? other.getIsDisassembled() == null : this.getIsDisassembled().equals(other.getIsDisassembled()))
            && (this.getTr() == null ? other.getTr() == null : this.getTr().equals(other.getTr()))
            && (this.getSapCode() == null ? other.getSapCode() == null : this.getSapCode().equals(other.getSapCode()))
            && (this.getPalletQuantity() == null ? other.getPalletQuantity() == null : this.getPalletQuantity().equals(other.getPalletQuantity()))
            && (this.getAfterPacking() == null ? other.getAfterPacking() == null : this.getAfterPacking().equals(other.getAfterPacking()))
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
        result = prime * result + ((getShippingPlanId() == null) ? 0 : getShippingPlanId().hashCode());
        result = prime * result + ((getShippingMark() == null) ? 0 : getShippingMark().hashCode());
        result = prime * result + ((getEtoPo() == null) ? 0 : getEtoPo().hashCode());
        result = prime * result + ((getEtoPlant() == null) ? 0 : getEtoPlant().hashCode());
        result = prime * result + ((getStockMovementDate() == null) ? 0 : getStockMovementDate().hashCode());
        result = prime * result + ((getCountry() == null) ? 0 : getCountry().hashCode());
        result = prime * result + ((getProdOrder() == null) ? 0 : getProdOrder().hashCode());
        result = prime * result + ((getQty() == null) ? 0 : getQty().hashCode());
        result = prime * result + ((getIsDisassembled() == null) ? 0 : getIsDisassembled().hashCode());
        result = prime * result + ((getTr() == null) ? 0 : getTr().hashCode());
        result = prime * result + ((getSapCode() == null) ? 0 : getSapCode().hashCode());
        result = prime * result + ((getPalletQuantity() == null) ? 0 : getPalletQuantity().hashCode());
        result = prime * result + ((getAfterPacking() == null) ? 0 : getAfterPacking().hashCode());
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
        sb.append(", shippingPlanId=").append(shippingPlanId);
        sb.append(", shippingMark=").append(shippingMark);
        sb.append(", etoPo=").append(etoPo);
        sb.append(", etoPlant=").append(etoPlant);
        sb.append(", stockMovementDate=").append(stockMovementDate);
        sb.append(", country=").append(country);
        sb.append(", prodOrder=").append(prodOrder);
        sb.append(", qty=").append(qty);
        sb.append(", isDisassembled=").append(isDisassembled);
        sb.append(", tr=").append(tr);
        sb.append(", sapCode=").append(sapCode);
        sb.append(", palletQuantity=").append(palletQuantity);
        sb.append(", afterPacking=").append(afterPacking);
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