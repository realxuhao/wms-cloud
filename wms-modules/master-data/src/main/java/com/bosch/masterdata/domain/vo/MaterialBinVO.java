package com.bosch.masterdata.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class MaterialBinVO {

    /**
     * id
     */
    private Long id;

    /**
     * 物料id
     */
    @ApiModelProperty(value = "物料id")
    private Long materialId;

    /**
     * 物料code
     */
    @ApiModelProperty(value = "物料code")
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(value = "物料名称")
    private String materialName;

    /**
     * 库位id
     */
    @ApiModelProperty(value = "库位id")
    private Long binId;

    /**
     * 库位code
     */
    @ApiModelProperty(value = "库位code")
    private String binCode;

    /**
     * 库位name
     */
    @ApiModelProperty(value = "库位name")
    private String binName;

    /**
     * 分配至该库位的优先级
     */
    @ApiModelProperty(value = "分配至该库位的优先级")
    private Long priorityLevel;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
