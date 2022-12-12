package com.bosch.binin.api.domain.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

public class MaterialReturnVO {
    /**
     * 主键id
     */
    @ApiModelProperty(value = "备注")
    private Long id;

    /**
     * 料号
     */
    @ApiModelProperty(value = "备注")
    private String materialCode;

    /**
     * SSCC码
     */
    @ApiModelProperty(value = "备注")
    private String ssccNumber;


    /**
     * 批次号
     */
    @ApiModelProperty(value = "批次号")
    private String batchNb;
    /**
     * 退库类型 (0：正常退库,1：异常退库)
     */
    @ApiModelProperty(value = "备注")
    private Integer type;

    /**
     * 数量
     */
    @ApiModelProperty(value = "备注")
    private BigDecimal quantity;

    /**
     * 移动类型 （0： 收货，1：入库，2：上架，3：生产叫料）
     */
    @ApiModelProperty(value = "备注")
    private String moveType;

    /**
     * (-1,"取消退库")
     (0,"正常")
     */
    @ApiModelProperty(value = "备注")
    private Integer status;
    /**
     * 存储区id
     */
    @ApiModelProperty(value = "areaId")
    private Long areaId;

    /**
     * 存储区code
     */
    @ApiModelProperty(value = "areaCode")
    private String areaCode;

    /**
     * 删除标记1：删除，0:可用
     */
    private Integer deleteFlag;
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

    /** 备注 */
    @ApiModelProperty(value = "备注")
    private String remark;
}
