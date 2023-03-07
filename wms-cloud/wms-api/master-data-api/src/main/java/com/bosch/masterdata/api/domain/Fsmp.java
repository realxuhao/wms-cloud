package com.bosch.masterdata.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * ecn主数据
 * @TableName md_ecn
 */
@TableName(value ="md_fsmp")
@Data
public class Fsmp implements Serializable {
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
     * 创建人
     */
    private String createBy;

    /**
     * 修改人
     */
    private String updateBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 状态（1：启用，0：停用）
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 类别
     */
    private String classification;

    /**
     * 删除标记1：删除，0:可用
     */
    private Integer deleteFlag;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}