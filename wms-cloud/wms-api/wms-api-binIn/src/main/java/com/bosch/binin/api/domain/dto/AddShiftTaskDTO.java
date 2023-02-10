package com.bosch.binin.api.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-11-17 09:52
 **/
@Data
public class AddShiftTaskDTO {

    @ApiModelProperty(value = "sscc码")
    private List<String> ssccNbList;

    @ApiModelProperty(value = "目的仓库")
    private String targetWareCode;

    @ApiModelProperty(value = "目的工厂")
    private String targetPlantNb;

}
