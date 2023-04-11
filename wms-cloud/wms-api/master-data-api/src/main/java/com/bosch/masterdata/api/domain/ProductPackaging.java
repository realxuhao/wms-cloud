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
public class ProductPackaging extends BaseEntity {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;


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

    private String productName;

    /**
     * Cell
     */
    private String cell;

    /**
     * 运输单位(Tr)
     */
    private String transportUnit;

    /**
     * 箱 Tr 对应包装规格
     */
    private String boxSpecification;

    /**
     * 标准 Tr/托
     */
    private String standardUnits;

    /**
     * 重量（Tr）
     */
    private String weight;

    /**
     * 体积 (Tr)
     */
    private String volume;
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}