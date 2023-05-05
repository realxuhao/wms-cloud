package com.bosch.binin.api.enumeration;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-05-05 09:29
 **/
public enum WeightStatusEnum {
    //已取消
    NOT_USE(0, "未使用"),
    //待下发
    USED(1, "已使用");

    private Integer code;

    private String desc;

    WeightStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer code() {
        return this.code;
    }
}
