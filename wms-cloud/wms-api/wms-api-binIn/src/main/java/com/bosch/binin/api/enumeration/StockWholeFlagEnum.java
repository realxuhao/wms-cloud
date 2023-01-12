package com.bosch.binin.api.enumeration;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-11-22 15:09
 **/
public enum StockWholeFlagEnum {
    //已取消
    WHOLE(0, "整托"),
    //待下发
    NOT_WHOLE(1, "零托");

    private Integer code;

    private String desc;

    StockWholeFlagEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer code() {
        return this.code;
    }
}
