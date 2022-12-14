package com.bosch.binin.api.domain.dto;

import com.ruoyi.common.core.web.page.PageDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MaterialReturnDTO extends PageDomain {


    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 料号
     */
    @ApiModelProperty(value = "materialCode")
    private String materialCode;

    /**
     * SSCC码
     */
    @ApiModelProperty(value = "ssccNumber")
    private String ssccNumber;
    /**
     * 批次号
     */
    @ApiModelProperty(value = "批次号")
    private String batchNb;
    /**
     * 退库类型 (0：正常退库,1：异常退库)
     */
    @ApiModelProperty(value = "type")
    private Integer type;

    /**
     * 数量
     */
    @ApiModelProperty(value = "quantity")
    private BigDecimal quantity;

    /**
     * 移动类型 （0： 收货，1：入库，2：上架，3：生产叫料）
     */
    @ApiModelProperty(value = "moveType")
    private String moveType;

    /**
     * (-1,"取消退库")
     (0,"正常")
     */
    @ApiModelProperty(value = "status")
    private Integer status;
    /**
     * 存储区id
     */
    @ApiModelProperty(value = "areaId")
    private Long areaId;

    /**
     * 存储区code
     */
    @ApiModelProperty(value = "areaCode")
    private String areaCode;

    /**
     * 删除标记1：删除，0:可用
     */
    @ApiModelProperty(value = "deleteFlag")
    private Integer deleteFlag;
}