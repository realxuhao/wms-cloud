package com.bosch.masterdata.api.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * nmd主数据
 * @TableName md_nmd
 */
@TableName(value ="md_nmd")
@Data
public class EcnVO implements Serializable {
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 物料code
     */
    @ApiModelProperty(value = "物料code")
    private String materialCode;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createBy;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private String updateBy;

    /**
     * 创建时间
     */

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改时间
     */

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 状态（1：启用，0：停用）
     */
    @ApiModelProperty(value = "状态（1：启用，0：停用）")
    private Integer status;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 类别（
     */
    @ApiModelProperty(value = "类别")
    private Integer classification;

    /**
     * 取样规则
     */
    @ExcelProperty(value = "取样规则")
    @ApiModelProperty(value = "取样规则")
    private String plan;

}