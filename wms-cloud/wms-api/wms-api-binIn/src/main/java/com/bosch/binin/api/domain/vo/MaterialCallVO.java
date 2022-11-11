package com.bosch.binin.api.domain.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.bosch.binin.api.domain.Stock;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-11-09 14:50
 **/
@Data
public class MaterialCallVO {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String orderNb;

    /**
     * 物料号
     */
    @ApiModelProperty(value = "物料号")
    private String materialNb;

    /**
     * 物料名称
     */
    @ApiModelProperty(value = "物料名称")
    private String materialName;


    /**
     * 数量
     */
    @ApiModelProperty(value = "数量")
    private Double quantity;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位")
    private String unit;


    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 部门
     */
    @ApiModelProperty(value = "部门")
    private String cell;

    /**
     * 排序类型
     */
    @ApiModelProperty(value = "排序类型")
    private Integer sortType;

    /** 创建者 */
    @ApiModelProperty(value = "创建者")
    private String createBy;

    /** 创建时间 */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新者 */
    @ApiModelProperty(value = "更新者")
    private String updateBy;

    /** 更新时间 */
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;


}
