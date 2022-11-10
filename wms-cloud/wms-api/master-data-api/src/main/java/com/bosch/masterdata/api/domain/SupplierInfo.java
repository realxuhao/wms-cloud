package com.bosch.masterdata.api.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * 供应商对象 md_supplier_info
 * 
 * @author xuhao
 * @date 2022-09-22
 */
@Data
@TableName("md_supplier_info")
public class SupplierInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 供应商编码 */
    @Excel(name = "供应商编码")
    private String code;

    /** 供应商名称 */
    @Excel(name = "供应商名称")
    private String name;

    /** 供应商时间窗口 */
    @Excel(name = "供应商时间窗口")
    private Long timeWindow;

    /** 状态（1：启用，0：停用） */
    @Excel(name = "状态", readConverterExp = "1=：启用，0：停用")
    private Long status;


}
