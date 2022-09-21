package com.bosch.maindata.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * 托盘对象 pallet
 * 
 * @author xuhao
 * @date 2022-09-21
 */
public class Pallet extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 托盘类型 */
    @Excel(name = "托盘类型")
    private String type;

    /** 托盘长度 */
    @Excel(name = "托盘长度")
    private BigDecimal length;

    /** 托盘宽度 */
    @Excel(name = "托盘宽度")
    private BigDecimal width;

    /** 托盘高度 */
    @Excel(name = "托盘高度")
    private BigDecimal height;

    /** 虚拟托盘前缀编码 */
    @Excel(name = "虚拟托盘前缀编码")
    private String virtualPrefixCode;

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
    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
    }
    public void setLength(BigDecimal length) 
    {
        this.length = length;
    }

    public BigDecimal getLength() 
    {
        return length;
    }
    public void setWidth(BigDecimal width) 
    {
        this.width = width;
    }

    public BigDecimal getWidth() 
    {
        return width;
    }
    public void setHeight(BigDecimal height) 
    {
        this.height = height;
    }

    public BigDecimal getHeight() 
    {
        return height;
    }
    public void setVirtualPrefixCode(String virtualPrefixCode) 
    {
        this.virtualPrefixCode = virtualPrefixCode;
    }

    public String getVirtualPrefixCode() 
    {
        return virtualPrefixCode;
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
            .append("type", getType())
            .append("length", getLength())
            .append("width", getWidth())
            .append("height", getHeight())
            .append("virtualPrefixCode", getVirtualPrefixCode())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("status", getStatus())
            .append("remark", getRemark())
            .toString();
    }
}
