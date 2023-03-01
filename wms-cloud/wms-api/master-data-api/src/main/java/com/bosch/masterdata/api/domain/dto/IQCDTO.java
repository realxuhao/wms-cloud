package com.bosch.masterdata.api.domain.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.web.page.PageDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Objects;


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

    /**
     * 重写equals方法
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof IQCDTO)) return false;
        IQCDTO iqcdto = (IQCDTO) o;
        return Objects.equals(plantNb, iqcdto.plantNb) && Objects.equals(SSCCNumber, iqcdto.SSCCNumber)
                && Objects.equals(finalSAPStatus, iqcdto.finalSAPStatus) && Objects.equals(sapProcessStatus, iqcdto.sapProcessStatus)
                && Objects.equals(identification, iqcdto.identification);
    }

    /**
     * 重写hashCode方法
     */
    @Override
    public int hashCode() {
        return Objects.hash(plantNb, SSCCNumber, finalSAPStatus, sapProcessStatus, identification);
    }
}