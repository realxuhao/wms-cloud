package com.bosch.maindata.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * 物料类型对象 item_type
 * 
 * @author xuhao
 * @date 2022-09-21
 */
@Data
public class ItemType extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 物料类型代码 */
    @ApiModelProperty(value = "物料类型代码")
    @Excel(name = "物料类型代码")
    private String code;

    /** 物料类型描述 */
    @ApiModelProperty(value = "物料类型描述")
    @Excel(name = "物料类型描述")
    private String description;

    /** 所属部门 */
    @ApiModelProperty(value = "所属部门")
    @Excel(name = "所属部门")
    private Long cellId;

    /** 状态（1：启用，0：停用） */
    @ApiModelProperty(value = "状态 1：启用，0：停用")
    @Excel(name = "状态", readConverterExp = "1：启用，0：停用")
    private Long status;

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
    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }
    public void setCellId(Long cellId) 
    {
        this.cellId = cellId;
    }

    public Long getCellId() 
    {
        return cellId;
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
            .append("description", getDescription())
            .append("cellId", getCellId())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("status", getStatus())
            .append("remark", getRemark())
            .toString();
    }
}
