package com.bosch.binin.api.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.util.Date;

import com.ruoyi.common.core.web.domain.BaseEntity;
import lombok.Data;

/**
 * 退库表
 * @TableName material_return
 */
@TableName(value ="material_return")
@Data
public class MaterialReturn extends BaseEntity {
    /**
     * 主键id
     */
    private Long id;

    /**
     * 料号
     */
    private String materialNb;

    /**
     * SSCC码
     */
    private String ssccNumber;

    /**
     * 批次号
     */
    private String batchNb;

    /**
     * 退库类型 (0：正常退库,1：异常退库)
     */
    private Integer type;

    /**
     * 数量
     */
    private Double quantity;

    /**
     * 移动类型 （0： 收货，1：入库，2：上架，3：生产叫料）
     */
    private String moveType;

    /**
     * (-1,"取消退库")
(0,"正常")
     */
    private Integer status;

    /**
     * 存储区code
     */
    private String areaCode;

    /**
     * 仓库编码
     */
    private String wareCode;

    private Date expireDate;

    private String cell;

    /**
     * 删除标记1：删除，0:可用
     */
    private Integer deleteFlag;

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
        MaterialReturn other = (MaterialReturn) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getMaterialNb() == null ? other.getMaterialNb() == null : this.getMaterialNb().equals(other.getMaterialNb()))
            && (this.getSsccNumber() == null ? other.getSsccNumber() == null : this.getSsccNumber().equals(other.getSsccNumber()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getQuantity() == null ? other.getQuantity() == null : this.getQuantity().equals(other.getQuantity()))
            && (this.getMoveType() == null ? other.getMoveType() == null : this.getMoveType().equals(other.getMoveType()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getCreateBy() == null ? other.getCreateBy() == null : this.getCreateBy().equals(other.getCreateBy()))
            && (this.getUpdateBy() == null ? other.getUpdateBy() == null : this.getUpdateBy().equals(other.getUpdateBy()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
            && (this.getDeleteFlag() == null ? other.getDeleteFlag() == null : this.getDeleteFlag().equals(other.getDeleteFlag()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getMaterialNb() == null) ? 0 : getMaterialNb().hashCode());
        result = prime * result + ((getSsccNumber() == null) ? 0 : getSsccNumber().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getQuantity() == null) ? 0 : getQuantity().hashCode());
        result = prime * result + ((getMoveType() == null) ? 0 : getMoveType().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getCreateBy() == null) ? 0 : getCreateBy().hashCode());
        result = prime * result + ((getUpdateBy() == null) ? 0 : getUpdateBy().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getDeleteFlag() == null) ? 0 : getDeleteFlag().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", materialCode=").append(materialNb);
        sb.append(", ssccNumber=").append(ssccNumber);
        sb.append(", type=").append(type);
        sb.append(", quantity=").append(quantity);
        sb.append(", moveType=").append(moveType);
        sb.append(", status=").append(status);
        sb.append(", deleteFlag=").append(deleteFlag);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}