package com.bosch.storagein.api.domain;

import lombok.Data;

import java.util.Date;

/**
 * @program: wms-cloud
 * @description: 称重数据
 * @author: xuhao
 * @create: 2023-02-17 09:39
 **/

@Data
public class Weight {
    private Long id;
    private String ip;
    private Integer port;
    private Double weight;
    private Date uploadTime;
    private int status;
    private String ssccNb;

}
