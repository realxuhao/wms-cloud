package com.bosch.masterdata.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * 区域对象 md_area
 * 
 * @author xuhao
 * @date 2022-09-26
 */
public class Area extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Integer id;

    /** 存储区编码 */
    @Excel(name = "存储区编码")
    private String code;

    /** 存储区名称 */
    @Excel(name = "存储区名称")
    private String name;

    /** 仓库id */
    @Excel(name = "仓库id")
    private Integer wareId;

    /** 状态（1：启用，0：停用） */
    @Excel(name = "状态", readConverterExp = "1=：启用，0：停用")
    private Long status;

    public void setId(Integer id) 
    {
        this.id = id;
    }

    public Integer getId() 
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
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setWareId(Integer wareId) 
    {
        this.wareId = wareId;
    }

    public Integer getWareId() 
    {
        return wareId;
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
            .append("code", getCode())
            .append("name", getName())
            .append("wareId", getWareId())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("status", getStatus())
            .append("remark", getRemark())
            .toString();
    }
}
