package com.bosch.masterdata.api.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * 库位对象 md_bin
 * 
 * @author xuhao
 * @date 2022-09-26
 */
@Data
@TableName("md_bin")
public class Bin extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 跨id */
    @Excel(name = "跨id")
    private Long frameId;

    /** 库位名称 */
    @Excel(name = "库位名称")
    private String name;

    /** 库位编码 */
    @Excel(name = "库位编码")
    private String code;

    /** 状态（1：启用，0：停用） */
    @Excel(name = "状态", readConverterExp = "1=：启用，0：停用")
    private Long status;


}
