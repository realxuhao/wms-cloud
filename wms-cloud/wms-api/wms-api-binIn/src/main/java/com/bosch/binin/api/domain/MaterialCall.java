package com.bosch.binin.api.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.Map;

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
    private String orderNb;

    /**
     * 物料号
     */
    private String materialNb;

    /**
     * 物料名称
     */
    private String materialName;

    /**
     * 需求量
     */
    private Double quantity;

    /**
     * 已下发量
     */
    private Double issuedQuantity;

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

    /**
     * 状态：0:未下发，1：部分下发，2：已全部下发
     */
    private Integer status;

    /**
     * 未下发量
     */
    @TableField(exist = false)
    private Double unIssuedQuantity;






}
