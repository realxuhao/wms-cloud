package com.bosch.binin.api.enumeration;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-12-20 14:55
 **/
public enum ManuTransStatusEnum {
    //待下发
    WAITING_ISSUE(0, "未下发"),

    //待下架
    WAITING_BIN_DOWN(1, "待下架"),

    FINISH(2, "完成");



    private Integer code;

    private String desc;

    ManuTransStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer code() {
        return this.code;
    }
}
