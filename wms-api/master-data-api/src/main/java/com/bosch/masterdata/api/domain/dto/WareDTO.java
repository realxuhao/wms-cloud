package com.bosch.masterdata.api.domain.dto;

import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.web.page.PageDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WareDTO extends PageDomain {
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /** 工厂编码 */
    @ApiModelProperty(name = "工厂编码")
    private String factoryCode;

    /** 工厂名称 */
    @ApiModelProperty(name = "工厂名称")
    private String factoryName;

    /**
     * 仓库编码
     */
    @ApiModelProperty(value = "仓库编码")
    private String code;

    /**
     * 仓库名称
     */
    @ApiModelProperty(value = "仓库名称")
    private String name;

    /**
     * 仓库地址
     */
    @ApiModelProperty(value = "仓库地址")
    private String location;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

}
