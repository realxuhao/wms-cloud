package com.bosch.binin.api.domain.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class MaterialReturnVO {
    /**
     * 主键id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 料号
     */
    @ApiModelProperty(value = "物料号")
    private String materialNb;


    /**
     * 料号
     */
    @ApiModelProperty(value = "物料名称")
    private String materialName;

    /**
     * SSCC码
     */
    @ApiModelProperty(value = "SSCC")
    private String ssccNumber;


    /**
     * 批次号
     */
    @ApiModelProperty(value = "批次号")
    private String batchNb;

    /**
     * 退库类型 (0：正常退库,1：异常退库)
     */
    @ApiModelProperty(value = "退库类型 (0：正常退库,1：异常退库)")
    private Integer type;


    /**
     * 仓库编码
     */
    @ApiModelProperty(value = "仓库编码")
    private String wareCode;

    @ApiModelProperty(value = "过期时间")
    private Date expireDate;

    /**
     * 数量
     */
    @ApiModelProperty(value = "数量")
    private BigDecimal quantity;

    /**
     * 移动类型 （0： 收货，1：入库，2：上架，3：生产叫料）
     */
    @ApiModelProperty(value = " 移动类型 （0： 收货，1：入库，2：上架，3：生产叫料）")
    private String moveType;

    /**
     * (-1,"取消退库")
     (0,"正常")
     */
    @ApiModelProperty(value = "-1:取消任务,0:待确认,1:待上架,2:完成")
    private Integer status;

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
