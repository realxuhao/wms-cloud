package com.bosch.masterdata.api.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.web.page.PageDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class ProInOutStockDTO extends PageDomain {

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
     * 物料号
     */
    @ApiModelProperty(value = "material_nb")
    private String materialNb;

    /**
     * 批次号
     */
    @ApiModelProperty(value = "batch_nb")
    private String batchNb;
}
