package com.bosch.binin.api.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-12-20 13:57
 **/
@Data
public class AddManualTransDTO {


    @ApiModelProperty(value = "类型，0：正常，1：异常")
    private int type;

    @ApiModelProperty(value = "库存ID列表")
    private List<Long> idList;

    @ApiModelProperty(value = "目标区域编码")
    private String targetAreaCode;

//    @ApiModelProperty(value = "目标库位编码")
//    private String targetBinCode;

}
