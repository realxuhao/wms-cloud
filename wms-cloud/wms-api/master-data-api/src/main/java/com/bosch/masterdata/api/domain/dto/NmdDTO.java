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
     * 状态（1：启用，0：停用）
     */
    @ApiModelProperty(value = "状态（1：启用，0：停用）")
    private Integer status;

    /**
     * 物料code
     */
    @ExcelProperty(value = "物料code")
    @ApiModelProperty(value = "物料code")
    private String materialCode;

    /**
     * 类别（0：Components，1：Package）
     */
    @ExcelProperty(value = "类别")
    @ApiModelProperty(value = "类别")
    private Integer classification;

    /**
     * 检验水平级别
     */
    @ExcelProperty(value = "检验水平级别")
    @ApiModelProperty(value = "检验水平级别")
    private String level;

    /**
     * 抽样方案（1：正常,2：加严,3：放宽）
     */
    @ExcelProperty(value = "抽样方案")
    @ApiModelProperty(value = "抽样方案")
    private Integer plan;


}