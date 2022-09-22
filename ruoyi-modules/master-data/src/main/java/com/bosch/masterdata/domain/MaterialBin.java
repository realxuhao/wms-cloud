package com.bosch.masterdata.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * 物料库位分配策略对象 md_material_bin
 * 
 * @author xuhao
 * @date 2022-09-22
 */
public class MaterialBin extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 物料id */
    @Excel(name = "物料id")
    private Long materialId;

    /** 库位id */
    @Excel(name = "库位id")
    private Long binId;

    /** 分配至该库位的优先级 */
    @Excel(name = "分配至该库位的优先级")
    private Long priorityLevel;

    /** 状态（1：启用，0：停用） */
    @Excel(name = "状态", readConverterExp = "1=：启用，0：停用")
    private Long status;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setMaterialId(Long materialId) 
    {
        this.materialId = materialId;
    }

    public Long getMaterialId() 
    {
        return materialId;
    }
    public void setBinId(Long binId) 
    {
        this.binId = binId;
    }

    public Long getBinId() 
    {
        return binId;
    }
    public void setPriorityLevel(Long priorityLevel) 
    {
        this.priorityLevel = priorityLevel;
    }

    public Long getPriorityLevel() 
    {
        return priorityLevel;
    }
    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("materialId", getMaterialId())
            .append("binId", getBinId())
            .append("priorityLevel", getPriorityLevel())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("status", getStatus())
            .append("remark", getRemark())
            .toString();
    }
}
