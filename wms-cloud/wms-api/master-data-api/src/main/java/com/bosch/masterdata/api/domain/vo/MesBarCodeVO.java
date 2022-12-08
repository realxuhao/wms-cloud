package com.bosch.masterdata.api.domain.vo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-12-08 17:30
 **/
@Data
public class MesBarCodeVO {
    private Date expireDate;
    private String ssccNb;
    private String materialNb;
    private String batchNb;
    private Double quantity;
}
