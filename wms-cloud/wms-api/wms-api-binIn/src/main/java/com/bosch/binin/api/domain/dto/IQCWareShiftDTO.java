package com.bosch.binin.api.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-03-07 12:58
 **/
@Data
public class IQCWareShiftDTO {
    @NotNull
    private String sscc;
    @NotNull
    private String targetWareCode;

}
