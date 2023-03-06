package com.bosch.masterdata.api.domain.dto;

import com.ruoyi.common.core.web.page.PageDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BlackDriverDTO extends PageDomain {

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    private Long driverId;

    /**
     * 微信id
     */
    @ApiModelProperty(value = "微信id")
    private String wechatId;

    /**
     * 司机姓名
     */
    @ApiModelProperty(value = "司机姓名")
    private String driverName;

    /**
     * 司机联系方式
     */
    @ApiModelProperty(value = "司机联系方式")
    private String driverPhone;

    /**
     * 车牌
     */
    @ApiModelProperty(value = "车牌")
    private String carNum;

    /**
     * 状态：1：启用黑名单 0：禁用黑名单
     */
    @ApiModelProperty(value = "状态：1：启用黑名单 0：禁用黑名单")
    private Integer status;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

}
