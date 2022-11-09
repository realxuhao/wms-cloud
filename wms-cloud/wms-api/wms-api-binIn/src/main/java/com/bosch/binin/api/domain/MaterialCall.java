package com.bosch.binin.api.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.web.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * @program: wms-cloud
 * @description: 叫料
 * @author: xuhao
 * @create: 2022-11-09 14:17
 **/
@Data
@TableName("material_call")
public class MaterialCall extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 订单号
     */
    private String oderNb;

    /**
     * 物料号
     */
    private String materialNb;


    /**
     * 数量
     */
    private Double quantity;

    /**
     * 单位
     */
    private String unit;


    /**
     * 备注
     */
    private String remark;

    /**
     * 部门
     */
    private String cell;

    /**
     * 排序类型
     */
    private Integer sortType;

    /**
     * 删除标记，0：可用，1：已删除
     */
    private Integer deleteFlag;


}
