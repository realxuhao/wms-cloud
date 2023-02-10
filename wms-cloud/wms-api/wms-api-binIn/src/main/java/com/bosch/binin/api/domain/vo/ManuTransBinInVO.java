package com.bosch.binin.api.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-12-22 10:54
 **/
@Data
public class ManuTransBinInVO {

    /**
     * 工厂
     */
    @ApiModelProperty(value = "工厂")
    private String plantNb;

    /**
     * 物料号
     */
    @ApiModelProperty(value = "物料号")
    private String materialNb;

    /**
     * sscc
     */
    @ApiModelProperty(value = "sscc")
    private String ssccNumber;

    /**
     * 物料名称
     */
    @ApiModelProperty(value = "物料名称")
    private String materialName;

    /**
     * 批次号
     */
    @ApiModelProperty(value = "批次号")
    private String batchNb;


    /**
     * 过期时间
     */
    @ApiModelProperty(value = "过期时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expireDate;

    /**
     * 仓库编码
     */
    @ApiModelProperty(value = "仓库编码")
    private String wareCode;

    /**
     * 状态(0：待上架,1:已上架)
     */
    @ApiModelProperty(value = "status，状态(0：待上架,1:已上架)")
    private Integer status;

    /**
     * 数量
     */
    @ApiModelProperty(value = "数量")
    private Integer quantity;

    @ApiModelProperty(value = "类型，0：正常，1：异常")
    private int type;
}
