package com.bosch.binin.api.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-11-10 13:49
 **/
@Data
public class MaterialCallQueryDTO {
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
     * 开始上传时间
     */
    @ApiModelProperty(value = "开始上传时间")
    private Date startCreateTime;

    /**
     * 结束上传时间
     */
    @ApiModelProperty(value = "结束上传时间")
    private Date endCreateTime;
}
