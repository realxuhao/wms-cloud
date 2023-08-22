package com.bosch.binin.api.domain.dto;

import com.ruoyi.common.core.web.page.PageDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Required;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.List;


@Data
public class IQCChangeStatusDTO {
    /**
     * id
     */
    @NotNull
    @ApiModelProperty(value = "id")
    private Long id;


    /**
     * type
     */
    @NotNull
    @Pattern(regexp = "[0,1]", message = "同批次质检:1,此托质检:0")
    @ApiModelProperty(value = "同批次质检:1,此托质检:0")
    private String type;

    /**
     * changeStatus
     */
    @NotEmpty
    @ApiModelProperty(value = "qualityStatus")
    private String qualityStatus;


    @ApiModelProperty(value = "fromStatusList")
    private List<String> fromStatusList;


    private String batchNb;


    private String fromProdOrder;
}
