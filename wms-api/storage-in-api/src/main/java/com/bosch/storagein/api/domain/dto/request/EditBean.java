package com.bosch.storagein.api.domain.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

/**
 * @author: UWH4SZH
 * @since: 10/11/2022 10:21
 * @description:
 */
@Data
public class EditBean {

    @ApiModelProperty(value = "id列表")
    private List<Long> ids;

    @ApiModelProperty(value = "是否可编辑0:可编辑，1：不可编辑")
    private Integer editFlag;
}
