package com.bosch.binin.api.domain.dto;

import lombok.Data;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-05-31 11:28
 **/
@Data
public class WareShiftBatchBinInDTO {

    private String mesBarCode;

    private String areaCode;

    private String qrCode;

}
