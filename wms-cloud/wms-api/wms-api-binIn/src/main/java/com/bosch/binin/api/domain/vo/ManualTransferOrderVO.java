package com.bosch.binin.api.domain.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-12-20 13:01
 **/
@Data
public class ManualTransferOrderVO {
    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "源工厂")
    private String sourcePlantNb;
    @ApiModelProperty(value = "源仓库")
    private String sourceWareCode;
    @ApiModelProperty(value = "源存储区")
    private String sourceAreaCode;
    @ApiModelProperty(value = "源库位")
    private String sourceBinCode;
    @ApiModelProperty(value = "移动类型")
    private String moveType;
    @ApiModelProperty(value = "类型，0：正常，1：异常")
    private int type;
    @ApiModelProperty(value = "sscc编码")
    private String ssccNb;
    @ApiModelProperty(value = "物料编码")
    private String materialNb;
    @ApiModelProperty(value = "物料名称")
    private String materialName;
    @ApiModelProperty(value = "目的存储区")
    private String targetAreaCode;
    @ApiModelProperty(value = "目的库位")
    private String targetBinCode;
    @ApiModelProperty(value = "状态")
    private Integer status;
    /**
     * 创建者
     */
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建者")
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新者
     */
    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "更新者")
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;
    @ApiModelProperty(value = "创建人")

    private Integer deleteFlag;

}
