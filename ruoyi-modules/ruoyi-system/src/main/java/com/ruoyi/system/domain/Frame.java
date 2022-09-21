package com.ruoyi.system.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * 跨对象 frame
 * 
 * @author ruoyi
 * @date 2022-09-19
 */
public class Frame extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 跨编码 */
    @Excel(name = "跨编码")
    private String code;

    /** 宽度 */
    @Excel(name = "宽度")
    private BigDecimal width;

    /** 承重 */
    @Excel(name = "承重")
    private BigDecimal bearWeight;

    /** 货架id */
    @Excel(name = "货架id")
    private Long shelveId;

    /** 仓库代码 */
    @Excel(name = "仓库代码")
    private String wareCode;

    /** 仓库名称 */
    @Excel(name = "仓库名称")
    private String wareName;

    /** 区域代码 */
    @Excel(name = "区域代码")
    private String areaCode;

    /** 区域名称 */
    @Excel(name = "区域名称")
    private String areaName;

    /** 货架代码 */
    @Excel(name = "货架代码")
    private String shelveCode;

    /** 货架名称 */
    @Excel(name = "货架名称")
    private String shelveName;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
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
    public void setShelveId(Long shelveId) 
    {
        this.shelveId = shelveId;
    }

    public Long getShelveId() 
    {
        return shelveId;
    }
    public void setWareCode(String wareCode) 
    {
        this.wareCode = wareCode;
    }

    public String getWareCode() 
    {
        return wareCode;
    }
    public void setWareName(String wareName) 
    {
        this.wareName = wareName;
    }

    public String getWareName() 
    {
        return wareName;
    }
    public void setAreaCode(String areaCode) 
    {
        this.areaCode = areaCode;
    }

    public String getAreaCode() 
    {
        return areaCode;
    }
    public void setAreaName(String areaName) 
    {
        this.areaName = areaName;
    }

    public String getAreaName() 
    {
        return areaName;
    }
    public void setShelveCode(String shelveCode) 
    {
        this.shelveCode = shelveCode;
    }

    public String getShelveCode() 
    {
        return shelveCode;
    }
    public void setShelveName(String shelveName) 
    {
        this.shelveName = shelveName;
    }

    public String getShelveName() 
    {
        return shelveName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("code", getCode())
            .append("width", getWidth())
            .append("bearWeight", getBearWeight())
            .append("shelveId", getShelveId())
            .append("wareCode", getWareCode())
            .append("wareName", getWareName())
            .append("areaCode", getAreaCode())
            .append("areaName", getAreaName())
            .append("shelveCode", getShelveCode())
            .append("shelveName", getShelveName())
            .append("remark", getRemark())
            .toString();
    }
}
