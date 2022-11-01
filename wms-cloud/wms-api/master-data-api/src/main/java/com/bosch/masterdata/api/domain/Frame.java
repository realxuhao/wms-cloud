package com.bosch.masterdata.api.domain;

import java.math.BigDecimal;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
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
@Data
@TableName("md_frame")
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

    /**
     * 跨类型编码
     */
    @Excel(name = "跨类型编码")
    private String typeCode;


    /**
     * 高度
     */
    @Excel(name = "高度")
    private BigDecimal height;

    /** 状态（1：启用，0：停用） */
    @Excel(name = "状态", readConverterExp = "1=：启用，0：停用")
    private Long status;

}
