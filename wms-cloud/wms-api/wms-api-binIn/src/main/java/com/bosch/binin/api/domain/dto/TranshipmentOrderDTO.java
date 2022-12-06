package com.bosch.binin.api.domain.dto;

import com.ruoyi.common.core.web.page.PageDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TranshipmentOrderDTO extends PageDomain {
    /**
     * 转运单号
     */
    @ApiModelProperty(value = "转运单号")
    private String orderNumber;

    /**
     * SSCC码
     */
    @ApiModelProperty(value = "SSCC码",required = true)
    private String ssccNumber;

    /**
     * 料号
     */
    @ApiModelProperty(value = "料号")
    private String materialCode;

    /**
     * 状态:状态：0:未执行，1:已执行
     *
     */
    @ApiModelProperty(value = "状态:状态：0:未执行，1:已执行")
    private Integer status;
    /**
     * deleteFlag
     */
    @ApiModelProperty(value = "deleteFlag")
    private Integer deleteFlag;
}
