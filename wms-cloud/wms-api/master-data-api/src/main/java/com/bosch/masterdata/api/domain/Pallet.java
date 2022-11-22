package com.bosch.masterdata.api.domain;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * 托盘对象 md_pallet
 * 
 * @author xuhao
 * @date 2022-09-22
 */
@Data
@TableName("md_pallet")
public class Pallet extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(type = IdType.AUTO)
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

    /**
     * deleteFlag
     */
    private Integer deleteFlag;

    /**
     * 是否是虚拟托盘 0：实物，1:虚拟
     */
    private Integer isVirtual;
}
