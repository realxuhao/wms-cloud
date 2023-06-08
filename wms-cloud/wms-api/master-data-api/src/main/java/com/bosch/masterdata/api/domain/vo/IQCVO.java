package com.bosch.masterdata.api.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class IQCVO {

    /**
     * PlantNb
     */
    @ExcelProperty(value = "PlantNb")
    @ApiModelProperty(value = "PlantNb")
    private String plantNb;

    /**
     * SSCCNumber
     */
    @ExcelProperty(value = "SSCCNumber")
    @ApiModelProperty(value = "SSCCNumber")
    private String ssccnumber;

    /**
     * J:FinalSAPStatus
     */
    @ExcelProperty(value = "FinalSAPStatus")
    @ApiModelProperty(value = "FinalSAPStatus")
    private String finalSAPStatus;

    /**
     * p:SAPProcessStatus
     */
    @ExcelProperty(value = "SAPProcessStatus")
    @ApiModelProperty(value = "SAPProcessStatus")
    private String sapProcessStatus;

    /**
     * ar:Identification
     */
    @ExcelProperty(value = "Identification")
    @ApiModelProperty(value = "Identification")
    private String identification;

    /**
     * status 0:成功 1:失败
     */
    @ApiModelProperty(value = "status 0:成功 1:失败")
    private Integer status;
    /**
     * 物料号
     */
    @ApiModelProperty(value = "物料号")
    @ExcelIgnore
    private String materialNb;

    /**
     * 物料名称
     */
    @ApiModelProperty(value = "物料名称")
    @ExcelIgnore
    private String materialName;
}
