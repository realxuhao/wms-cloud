package com.bosch.binin.api.domain.dto;

import com.ruoyi.common.core.web.page.PageDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-01-04 10:10
 **/
@Data
public class MaterialReturnQueryDTO extends PageDomain {
    private String wareCode;
    private String materialNb;

    private String areaCode;
    @ApiModelProperty(value = "-1:取消任务,0:待确认,1:待上架,2:完成")
    private Integer status;
    private Integer type;
}
