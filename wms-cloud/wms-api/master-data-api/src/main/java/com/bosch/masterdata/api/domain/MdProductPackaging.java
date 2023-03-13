package com.bosch.masterdata.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.ruoyi.common.core.web.domain.BaseEntity;
import lombok.Data;

/**
 * 成品包装量
 * @TableName md_product_packaging
 */
@TableName(value ="md_product_packaging")
@Data
public class MdProductPackaging extends BaseEntity {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;


    /**
     * 状态（1：启用，0：停用）
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 删除标记1：删除，0:可用
     */
    private Integer deleteFlag;

    /**
     * 成品料号
     */
    private String productNo;

    /**
     * Cell
     */
    private String cell;

    /**
     * 总数量/托
     */
    private String totalQuantity;

    /**
     * 最小包装数量/托
     */
    private String minQuantity;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}