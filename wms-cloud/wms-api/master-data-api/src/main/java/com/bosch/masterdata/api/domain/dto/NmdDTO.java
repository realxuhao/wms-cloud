package com.bosch.masterdata.api.domain.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.web.domain.BaseEntity;
import com.ruoyi.common.core.web.page.PageDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * nmd主数据
 * @TableName md_nmd
 */
@TableName(value ="md_nmd")
@Data
public class NmdDTO extends PageDomain {

    /**
     * id
     */
    private Long id;

    /**
     * 状态（1：启用，0：停用）
     */
    @ApiModelProperty(value = "状态（1：启用，0：停用）")
    private Integer status;

    /**
     * 物料code
     */
    @ExcelProperty(value = "料号")
    @ApiModelProperty(value = "料号")
    private String materialCode;

    /**
     * 类别（0：Components，1：Package）
     */
    @ExcelProperty(value = "分类")
    @ApiModelProperty(value = "类别")
    private Integer classification;

    /**
     * 检验水平级别
     */
    @ExcelProperty(value = "检验水平级别(s-1,s-2,s-3,s-4,Ⅰ,Ⅱ,Ⅲ)")
    @ApiModelProperty(value = "检验水平级别")
    private String level;

    /**
     * 抽样方案（1：正常,2：加严,3：放宽）
     */
    @ExcelProperty(value = "抽样方案（1：正常,2：加严,3：放宽）")
    @ApiModelProperty(value = "抽样方案")
    private Integer plan;

    /**
     * 备注
     */
    @ExcelProperty(value = "")
    @ApiModelProperty(value = "备注")
    private String remark;
}