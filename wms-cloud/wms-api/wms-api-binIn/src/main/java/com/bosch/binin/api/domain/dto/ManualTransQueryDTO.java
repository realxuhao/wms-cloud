package com.bosch.binin.api.domain.dto;

import com.ruoyi.common.core.web.page.PageDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-12-20 13:15
 **/
@Data
public class ManualTransQueryDTO extends PageDomain {

    private String materialNb;

    private String sourceWareCode;

    private String sourceBinCode;

    private String targetBinCode;

    private Integer status;

    @ApiModelProperty(value = "类型，0：正常，1：异常")
    private int type;


    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建者")
    private String createBy;

    /**
     * 开始创建时间
     */
    @ApiModelProperty(value = "开始创建时间")
    private Date startCreateTime;

    /**
     * 结束创建时间
     */
    @ApiModelProperty(value = "结束创建时间")
    private Date endCreateTime;

    /**
     * 开始更新时间
     */
    @ApiModelProperty(value = "开始更新时间")
    private Date startUpdateTime;

    /**
     * 结束更新时间
     */
    @ApiModelProperty(value = "结束更新时间")
    private Date endUpdateTime;

}
