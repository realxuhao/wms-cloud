package com.bosch.binin.api.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-11-29 13:25
 **/
@Data
public class WaitingBinDownVO {

    /**
     * 是否存在多个任务
     */
    @ApiModelProperty(value = "是否存在多个任务")
    private boolean multiJobFlag;

    /**
     * 是否存在多个任务
     */
    @ApiModelProperty(value = "任务列表")
    private List<MaterialKanbanVO> kanbanVOList;
}
