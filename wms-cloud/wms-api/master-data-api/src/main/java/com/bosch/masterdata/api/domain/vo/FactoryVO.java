package com.bosch.masterdata.api.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: wms-cloud
 * @description: 工厂
 * @author: xuhao
 * @create: 2022-11-10 13:25
 **/
@Data
public class FactoryVO {

    /** 工厂编码 */
    @ApiModelProperty(name = "工厂编码")
    private String factoryCode;

    /** 工厂名称 */
    @ApiModelProperty(name = "工厂名称")
    private String factoryName;
}
