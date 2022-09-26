package com.bosch.masterdata.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class BinVO {
    /** id */
    @ApiModelProperty(value = "id")
    private Long id;

    /** 跨id */
    @ApiModelProperty(value = "跨id")
    private Long frameId;

    /** 库位编码 */
    @ApiModelProperty(value = "库位编码")
    private String code;

    /** 库位名称 */
    @ApiModelProperty(value = "库位名称")
    private String name;


    /** 仓库id */
    @ApiModelProperty(value = "id")
    private Long wareId;

    /** 仓库编码 */
    @ApiModelProperty(value = "仓库编码")
    private String wareCode;

    /** 仓库名称 */
    @ApiModelProperty(value = "仓库名称")
    private String wareName;

    /** 区域id */
    @ApiModelProperty(value = "区域id")
    private Long areaId;

    /** 区域名称 */
    @ApiModelProperty(value = "区域名称")
    private String areaName;

    /** 区域编码 */
    @ApiModelProperty(value = "区域编码")
    private String areaCode;

    /** 跨编码 */
    @ApiModelProperty(value = "跨编码")
    private String frameCode;

    /** 跨名称 */
    @ApiModelProperty(value = "跨名称")
    private String frameName;

    /** 宽度 */
    @ApiModelProperty(value = "宽度")
    private BigDecimal frameWidth;

    /** 承重 */
    @ApiModelProperty(value = "承重")
    private BigDecimal frameBearWeight;

    /** 备注 */
    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
