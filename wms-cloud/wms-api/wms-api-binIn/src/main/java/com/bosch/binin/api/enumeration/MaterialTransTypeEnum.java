package com.bosch.binin.api.enumeration;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-12-20 14:43
 **/
public enum MaterialTransTypeEnum {
    //正常
    NORMAL(0, "正常"),

    //异常
    AB_NORMAL(1, "异常");


    private Integer code;

    private String desc;

    MaterialTransTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer code() {
        return this.code;
    }
}
