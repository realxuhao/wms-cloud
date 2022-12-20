package com.bosch.binin.api.domain.dto;

import com.ruoyi.common.core.web.page.PageDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-11-10 13:49
 **/
@Data
public class MaterialCallQueryDTO extends PageDomain {
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
     * 部门
     */
    @ApiModelProperty(value = "部门")
    private String cell;

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

    @ApiModelProperty(value = "0=未下发,1=部分下发,2=已全部下发")
    private Integer status;

    @ApiModelProperty(value = "ids,取消需求的时候需要传")
    private List<Long> ids;


}
