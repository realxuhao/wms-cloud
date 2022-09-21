package com.bosch.maindata.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * 供应商物料对象 item_supplier
 * 
 * @author xuhao
 * @date 2022-09-21
 */
public class ItemSupplier extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 物料id */
    @Excel(name = "物料id")
    private Long itemInfoId;

    /** 供应商id */
    @Excel(name = "供应商id")
    private Long supplierInfoId;

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
    public void setItemInfoId(Long itemInfoId) 
    {
        this.itemInfoId = itemInfoId;
    }

    public Long getItemInfoId() 
    {
        return itemInfoId;
    }
    public void setSupplierInfoId(Long supplierInfoId) 
    {
        this.supplierInfoId = supplierInfoId;
    }

    public Long getSupplierInfoId() 
    {
        return supplierInfoId;
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
            .append("itemInfoId", getItemInfoId())
            .append("supplierInfoId", getSupplierInfoId())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("status", getStatus())
            .append("remark", getRemark())
            .toString();
    }
}
