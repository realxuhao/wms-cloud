package com.bosch.binin.api.domain.dto;

import com.ruoyi.common.core.web.page.PageDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-01-04 10:10
 **/
@Data
public class MaterialReturnQueryDTO extends PageDomain {
    @ApiModelProperty(value = "仓库编码")
    private String wareCode;
    @ApiModelProperty(value = "物料号")
    private String materialNb;
    @ApiModelProperty(value = "开始创建时间")
    private String areaCode;
    @ApiModelProperty(value = "-1:取消任务,0:待确认,1:待上架,2:完成")
    private Integer status;
    @ApiModelProperty(value = "退库类型 (0：正常退库,1：异常退库)")
    private Integer type;
    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建者")
    private String createBy;
    /**
     * 创建者
     */
    @ApiModelProperty(value = "更新人")
    private String updateBy;
    /**
     * 开始创建时间
     */
    @ApiModelProperty(value = "开始创建时间")
    private Date createTimeStart;

    /**
     * 结束创建时间
     */
    @ApiModelProperty(value = "结束创建时间")
    private Date createTimeEnd;

    /**
     * 开始更新时间
     */
    @ApiModelProperty(value = "开始更新时间")
    private Date updateTimeStart;

    /**
     * 结束更新时间
     */
    @ApiModelProperty(value = "结束更新时间")
    private Date updateTimeEnd;
}
