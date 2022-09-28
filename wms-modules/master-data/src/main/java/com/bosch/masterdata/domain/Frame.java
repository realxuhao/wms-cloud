package com.bosch.masterdata.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * 跨对象 md_frame
 * 
 * @author xuhao
 * @date 2022-09-26
 */
public class Frame extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 区域id */
    @Excel(name = "区域id")
    private Long areaId;

    /** 跨名称 */
    @Excel(name = "跨名称")
    private String name;

    /** 跨编码 */
    @Excel(name = "跨编码")
    private String code;

    /** 宽度 */
    @Excel(name = "宽度")
    private BigDecimal width;

    /** 承重 */
    @Excel(name = "承重")
    private BigDecimal bearWeight;

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
    public void setAreaId(Long areaId) 
    {
        this.areaId = areaId;
    }

    public Long getAreaId() 
    {
        return areaId;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setCode(String code) 
    {
        this.code = code;
    }

    public String getCode() 
    {
        return code;
    }
    public void setWidth(BigDecimal width) 
    {
        this.width = width;
    }

    public BigDecimal getWidth() 
    {
        return width;
    }
    public void setBearWeight(BigDecimal bearWeight) 
    {
        this.bearWeight = bearWeight;
    }

    public BigDecimal getBearWeight() 
    {
        return bearWeight;
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
            .append("areaId", getAreaId())
            .append("name", getName())
            .append("code", getCode())
            .append("width", getWidth())
            .append("bearWeight", getBearWeight())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("status", getStatus())
            .append("remark", getRemark())
            .toString();
    }
}
