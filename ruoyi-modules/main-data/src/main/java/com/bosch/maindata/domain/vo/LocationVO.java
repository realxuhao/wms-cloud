package com.bosch.maindata.domain.vo;

import com.ruoyi.common.core.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class LocationVO {
    /** 仓库代码 */
    @Excel(name = "仓库代码")
    private String wareCode;

    /** 仓库名称 */
    @Excel(name = "仓库名称")
    private String wareName;

    /** 仓库类型，1：内库，0：外库 */
    @Excel(name = "仓库类型，1：内库，0：外库")
    private Long wareType;

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

    /** 跨编码 */
    @Excel(name = "跨编码")
    private String code;

    /** 宽度 */
    @Excel(name = "宽度")
    private BigDecimal width;

    /** 承重 */
    @Excel(name = "承重")
    private BigDecimal bearWeight;


}
