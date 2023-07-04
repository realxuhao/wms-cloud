package com.bosch.masterdata.api.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.web.page.PageDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class EfficiencyDTO extends PageDomain {
    /**
     *
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * type
     */
    @ApiModelProperty(value = "type")
    private Integer type;
    /**
     * cell
     */
    @ApiModelProperty(value = "cell")
    private String cell;

    @ApiModelProperty(value = "orderNumber")
    private String orderNumber;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "create_by")
    private String createBy;

    /**
     * 创建时间起
     */
    @ApiModelProperty(value = "创建时间起")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeStart;

    /**
     * 创建时间止
     */
    @ApiModelProperty(value = "创建时间止")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeEnd;

    /**
     * 物料、成品号
     */
    @ApiModelProperty(value = "code")
    private String code;

    /**
     *
     */
    @ApiModelProperty(value = "sscc_number")
    private String ssccNumber;

    /**
     *
     */
    @ApiModelProperty(value = "operation_type")
    private Integer operationType;


}
