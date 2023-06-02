package com.bosch.masterdata.api.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import com.ruoyi.common.core.web.domain.BaseEntity;
import lombok.Data;

/**
 * nmd主数据
 * @TableName md_nmd
 */
@TableName(value ="md_nmd")
@Data
public class Nmd extends BaseEntity {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 物料code
     */
    private String materialCode;
    /**
     * 状态（1：启用，0：停用）
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 类别（0：Components，1：Package）
     */
    private Integer classification;

    /**
     * 检验水平级别
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String level;

    /**
     * 删除标记1：删除，0:可用
     */
    private Integer deleteFlag;
    /**
     * 抽样方案（1：正常,2：加严,3：放宽）
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer plan;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}