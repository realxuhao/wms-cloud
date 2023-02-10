package com.bosch.masterdata.api.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * 仓库对象 md_ware
 * 
 * @author xuhao
 * @date 2022-09-26
 */
@Data
@TableName("md_ware")
public class Ware extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 工厂编码 */
    @Excel(name = "工厂编码")
    private String factoryCode;

    /** 工厂名称 */
    @Excel(name = "工厂名称")
    private String factoryName;

    /** 仓库编码 */
    @Excel(name = "仓库编码")
    private String code;

    /** 仓库名称 */
    @Excel(name = "仓库名称")
    private String name;

    /** 仓库地址 */
    @Excel(name = "仓库地址")
    private String location;

    /** 状态（1：启用，0：停用） */
    @Excel(name = "状态", readConverterExp = "1=：启用，0：停用")
    private Long status;

    /** 仓库联系人 */
    @Excel(name = "仓库联系人")
    private String wareUser;

    /** 仓库联系人电话 */
    @Excel(name = "仓库联系人电话")
    private String wareUserPhone;

    /** 道口数量 */
     @Excel(name = "道口数量")
    private  Long dockNum;

}
