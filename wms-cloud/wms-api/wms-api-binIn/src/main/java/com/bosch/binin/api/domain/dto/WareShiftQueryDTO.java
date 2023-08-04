package com.bosch.binin.api.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.web.page.PageDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-12-07 14:34
 **/
@Data
public class WareShiftQueryDTO extends PageDomain {

    private String sourceWareCode;


    private String targetWareCode;

    private Integer status;

    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建者")
    private String createBy;

    /**
     * 开始创建时间
     */
    @ApiModelProperty(value = "开始创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String startCreateTime;

    /**
     * 结束创建时间
     */
    @ApiModelProperty(value = "结束创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String endCreateTime;

    /**
     * 开始更新时间
     */
    @ApiModelProperty(value = "开始更新时间")
    private String startUpdateTime;

    /**
     * 结束更新时间
     */
    @ApiModelProperty(value = "结束更新时间")
    private String endUpdateTime;

    @ApiModelProperty(value = "ssccNb")
    private String ssccNb;

    private Integer type;

    private String materialNb;

    private String batchNb;

    private String carNb;

    private String orderNb;
}
