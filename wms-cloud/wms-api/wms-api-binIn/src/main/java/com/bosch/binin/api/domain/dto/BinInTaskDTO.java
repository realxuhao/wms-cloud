package com.bosch.binin.api.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collections;

/**
 * @author: UWH4SZH
 * @since: 10/20/2022 14:24
 * @description:
 */
@Data
public class BinInTaskDTO extends BinAllocationDTO {

    @ApiModelProperty(value = "推荐库位")
    private String recommendBinCode;

    public static void main(String[] args) {
        String s="w.w.w";
        String[] split = s.split("\\.");

        System.out.println(split.length);
    }


}
