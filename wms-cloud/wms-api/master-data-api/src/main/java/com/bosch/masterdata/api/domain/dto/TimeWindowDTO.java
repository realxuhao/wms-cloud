package com.bosch.masterdata.api.domain.dto;

import com.ruoyi.common.core.web.page.PageDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TimeWindowDTO extends PageDomain {
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private String startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private String endTime;

    /**
     * 道口编码
     */
    @ApiModelProperty(value = "道口编码")
    private String windowCode;

    /**
     * 状态（1：启用，0：停用）
     */
    @ApiModelProperty(value = "状态（1：启用，0：停用）")
    private Long status;

}
