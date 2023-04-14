package com.bosch.product.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 
 * @TableName stock_take_plan
 */
@TableName(value ="stock_take_plan")
@Data
public class StockTakePlan implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 盘点计划编码
     */
    @TableField(value = "code")
    private String code;

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
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 删除标记1：删除，0:可用
     */
    @TableField(value = "delete_flag")
    private Integer deleteFlag;

    /**
     * 盘点cell
     */
    @TableField(value = "cell")
    private String cell;

    /**
     * 盘点仓库
     */
    @TableField(value = "ware_code")
    private String wareCode;

    /**
     * 盘点区域
     */
    @TableField(value = "area_code")
    private String areaCode;

    /**
     * 盘点类型，0:明盘，1:盲盘
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 盘点方式
     */
    @TableField(value = "method")
    private String method;

    /**
     * 创建时物料总数
     */
    @TableField(value = "created_material_quantity")
    private Integer createdMaterialQuantity;

    /**
     * 第一次下发时物料总数
     */
    @TableField(value = "first_issue_material_quantity")
    private Integer firstIssueMaterialQuantity;

    /**
     * 第二次下发时物料总数
     */
    @TableField(value = "second_issue_material_quantity")
    private Integer secondIssueMaterialQuantity;

    /**
     * 第三次下发时物料总数
     */
    @TableField(value = "third_issue_material_quantity")
    private Integer thirdIssueMaterialQuantity;

    /**
     * 总下发物料总数
     */
    @TableField(value = "total_issue_quantity")
    private Integer totalIssueQuantity;

    /**
     * 判断库位总数
     */
    @TableField(value = "take_bin_quantity")
    private Integer takeBinQuantity;

    /**
     * 差异库位数
     */
    @TableField(value = "diff_bin_quantity")
    private Integer diffBinQuantity;

    /**
     * 0:已创建，1:进行中，2:完成
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 物料类型：0:原材料，1:成品
     */
    @TableField(value = "material_type")
    private Integer materialType;

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
        StockTakePlan other = (StockTakePlan) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getCode() == null ? other.getCode() == null : this.getCode().equals(other.getCode()))
            && (this.getCreateBy() == null ? other.getCreateBy() == null : this.getCreateBy().equals(other.getCreateBy()))
            && (this.getUpdateBy() == null ? other.getUpdateBy() == null : this.getUpdateBy().equals(other.getUpdateBy()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
            && (this.getDeleteFlag() == null ? other.getDeleteFlag() == null : this.getDeleteFlag().equals(other.getDeleteFlag()))
            && (this.getCell() == null ? other.getCell() == null : this.getCell().equals(other.getCell()))
            && (this.getWareCode() == null ? other.getWareCode() == null : this.getWareCode().equals(other.getWareCode()))
            && (this.getAreaCode() == null ? other.getAreaCode() == null : this.getAreaCode().equals(other.getAreaCode()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getMethod() == null ? other.getMethod() == null : this.getMethod().equals(other.getMethod()))
            && (this.getCreatedMaterialQuantity() == null ? other.getCreatedMaterialQuantity() == null : this.getCreatedMaterialQuantity().equals(other.getCreatedMaterialQuantity()))
            && (this.getFirstIssueMaterialQuantity() == null ? other.getFirstIssueMaterialQuantity() == null : this.getFirstIssueMaterialQuantity().equals(other.getFirstIssueMaterialQuantity()))
            && (this.getSecondIssueMaterialQuantity() == null ? other.getSecondIssueMaterialQuantity() == null : this.getSecondIssueMaterialQuantity().equals(other.getSecondIssueMaterialQuantity()))
            && (this.getThirdIssueMaterialQuantity() == null ? other.getThirdIssueMaterialQuantity() == null : this.getThirdIssueMaterialQuantity().equals(other.getThirdIssueMaterialQuantity()))
            && (this.getTotalIssueQuantity() == null ? other.getTotalIssueQuantity() == null : this.getTotalIssueQuantity().equals(other.getTotalIssueQuantity()))
            && (this.getTakeBinQuantity() == null ? other.getTakeBinQuantity() == null : this.getTakeBinQuantity().equals(other.getTakeBinQuantity()))
            && (this.getDiffBinQuantity() == null ? other.getDiffBinQuantity() == null : this.getDiffBinQuantity().equals(other.getDiffBinQuantity()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getMaterialType() == null ? other.getMaterialType() == null : this.getMaterialType().equals(other.getMaterialType()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCode() == null) ? 0 : getCode().hashCode());
        result = prime * result + ((getCreateBy() == null) ? 0 : getCreateBy().hashCode());
        result = prime * result + ((getUpdateBy() == null) ? 0 : getUpdateBy().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getDeleteFlag() == null) ? 0 : getDeleteFlag().hashCode());
        result = prime * result + ((getCell() == null) ? 0 : getCell().hashCode());
        result = prime * result + ((getWareCode() == null) ? 0 : getWareCode().hashCode());
        result = prime * result + ((getAreaCode() == null) ? 0 : getAreaCode().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getMethod() == null) ? 0 : getMethod().hashCode());
        result = prime * result + ((getCreatedMaterialQuantity() == null) ? 0 : getCreatedMaterialQuantity().hashCode());
        result = prime * result + ((getFirstIssueMaterialQuantity() == null) ? 0 : getFirstIssueMaterialQuantity().hashCode());
        result = prime * result + ((getSecondIssueMaterialQuantity() == null) ? 0 : getSecondIssueMaterialQuantity().hashCode());
        result = prime * result + ((getThirdIssueMaterialQuantity() == null) ? 0 : getThirdIssueMaterialQuantity().hashCode());
        result = prime * result + ((getTotalIssueQuantity() == null) ? 0 : getTotalIssueQuantity().hashCode());
        result = prime * result + ((getTakeBinQuantity() == null) ? 0 : getTakeBinQuantity().hashCode());
        result = prime * result + ((getDiffBinQuantity() == null) ? 0 : getDiffBinQuantity().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getMaterialType() == null) ? 0 : getMaterialType().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", code=").append(code);
        sb.append(", createBy=").append(createBy);
        sb.append(", updateBy=").append(updateBy);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", remark=").append(remark);
        sb.append(", deleteFlag=").append(deleteFlag);
        sb.append(", cell=").append(cell);
        sb.append(", wareCode=").append(wareCode);
        sb.append(", areaCode=").append(areaCode);
        sb.append(", type=").append(type);
        sb.append(", method=").append(method);
        sb.append(", createdMaterialQuantity=").append(createdMaterialQuantity);
        sb.append(", firstIssueMaterialQuantity=").append(firstIssueMaterialQuantity);
        sb.append(", secondIssueMaterialQuantity=").append(secondIssueMaterialQuantity);
        sb.append(", thirdIssueMaterialQuantity=").append(thirdIssueMaterialQuantity);
        sb.append(", totalIssueQuantity=").append(totalIssueQuantity);
        sb.append(", takeBinQuantity=").append(takeBinQuantity);
        sb.append(", diffBinQuantity=").append(diffBinQuantity);
        sb.append(", status=").append(status);
        sb.append(", materialType=").append(materialType);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}