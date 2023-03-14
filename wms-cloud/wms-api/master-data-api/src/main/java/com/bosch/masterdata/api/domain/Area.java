package com.bosch.masterdata.api.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.web.domain.BaseEntity;
import org.ehcache.javadoc.PrivateApi;

/**
 * 区域对象 md_area
 * 
 * @author xuhao
 * @date 2022-09-26
 */
@Data
@TableName("md_area")
public class Area extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 存储区编码 */
    @Excel(name = "存储区编码")
    private String code;

    /** 存储区名称 */
    @Excel(name = "存储区名称")
    private String name;

    /** 仓库id */
    @Excel(name = "仓库id")
    private Long wareId;

    /** 状态（1：启用，0：停用） */
    @Excel(name = "状态", readConverterExp = "1=：启用，0：停用")
    private Long status;

    /** 存储区类型 */
    @Excel(name = "存储区类型")
    private Integer areaType;


}
