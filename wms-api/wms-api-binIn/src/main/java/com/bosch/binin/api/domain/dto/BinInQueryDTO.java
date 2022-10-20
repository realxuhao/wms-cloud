package com.bosch.binin.api.domain.dto;

import com.ruoyi.common.core.web.page.PageDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: UWH4SZH
 * @since: 10/18/2022 14:15
 * @description:
 */
@Data
public class BinInQueryDTO extends PageDomain {

    /**
     * 物料号
     */
    @ApiModelProperty(value = "materialNb")
    private String materialNb;

    /**
     * 物料名称
     */
    @ApiModelProperty(value = "materialName")
    private String materialName;

    /**
     * 批次号
     */
    @ApiModelProperty(value = "batchNb")
    private String batchNb;

    /**
     * 仓库编码
     */
    @ApiModelProperty(value = "wareCode")
    private String wareCode;

    /**
     * 实际库位编码
     */
    @ApiModelProperty(value = "actualBinCode")
    private String actualBinCode;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "createUser", hidden = true)
    private String createUser;


    /**
     * 状态(0：待入库,1:已入库)
     */
    @ApiModelProperty(value = "state")
    private Integer state;

}
