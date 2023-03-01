package com.bosch.masterdata.api.domain.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.web.page.PageDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;



@Data
public class IQCDTO  {



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
    private String SSCCNumber;

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

}