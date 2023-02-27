package com.bosch.vehiclereservation.api.domain.dto;

import com.ruoyi.common.core.web.page.PageDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class DriverDispatchDTO extends PageDomain {

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    private Long dispatchId;

    /**
     * 仓库id
     */
    @ApiModelProperty(value = "仓库id")
    private Long wareId;

    /**
     * 道口编号
     */
    @ApiModelProperty(value = "道口编号")
    private String dockCode;

    /**
     * 订单类型：0：送货 1：取货
     */
    @ApiModelProperty(value = "订单类型：0：送货 1：取货")
    private Integer driverType;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sortNo;

    /**
     * 进厂时间
     */
    @ApiModelProperty(value = "进厂时间")
    private Date comeinDate;

    /**
     * 完成时间
     */
    @ApiModelProperty(value = "完成时间")
    private Date completeDate;

    /**
     * 状态：0：等待 1：进厂 2：完成
     */
    @ApiModelProperty(value = "状态：0：等待 1：进厂 2：完成")
    private Integer status;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 状态：0：等待 1：进厂 2：完成
     */
    @ApiModelProperty(value = "状态：0：等待 1：进厂 2：完成")
    private List<Integer> statusList;
}
