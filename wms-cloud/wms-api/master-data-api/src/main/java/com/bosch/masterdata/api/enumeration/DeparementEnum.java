package com.bosch.masterdata.api.enumeration;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-02-13 13:24
 **/
public enum DeparementEnum {
    NMD("NMD"),

    FSMP("FSMP");


    private String code;

    private DeparementEnum(String code) {
        this.code = code;
    }


    public String getCode() {
        return this.code;
    }
}
