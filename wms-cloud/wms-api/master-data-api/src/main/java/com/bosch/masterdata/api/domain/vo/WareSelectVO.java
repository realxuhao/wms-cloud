package com.bosch.masterdata.api.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: wms-cloud
 * @description: 仓库
 * @author: taojd
 * @create: 2023-02-10 13:51
 **/
@Data
public class WareSelectVO {
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 仓库编码
     */
    @ApiModelProperty(name = "仓库编码")
    private String code;

    /**
     * 道口数量
     */
    @ApiModelProperty(value = "道口数量")
    private Long dockNum;
}
