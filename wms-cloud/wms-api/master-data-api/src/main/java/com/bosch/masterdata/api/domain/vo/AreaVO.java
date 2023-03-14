package com.bosch.masterdata.api.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class AreaVO {
    /** id */
    @ApiModelProperty(value = "id")
    private Long id;

    /** 仓库id */
    @ApiModelProperty(value = "仓库id")
    private Long wareId;

    /** 仓库编码 */
    @ApiModelProperty(value = "仓库编码")
    private String wareCode;

    /** 仓库名称 */
    @ApiModelProperty(value = "仓库名称")
    private String wareName;

    /** 存储区编码 */
    @ApiModelProperty(value = "存储区编码")
    private String code;

    /** 存储区名称 */
    @ApiModelProperty(value = "存储区名称")
    private String name;

    /** 备注 */
    @ApiModelProperty(value = "备注")
    private String remark;
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


    /** 存储区类型 */
    @ApiModelProperty(value = "存储区类型")
    private Integer areaType;

    private String plantNb;
}
