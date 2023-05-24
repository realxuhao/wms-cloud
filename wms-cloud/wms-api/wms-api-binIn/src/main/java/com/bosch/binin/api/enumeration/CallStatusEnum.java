package com.bosch.binin.api.enumeration;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-11-22 15:09
 **/
public enum CallStatusEnum {
    //已取消
    CANCEL(-1, "已取消"),
    //待下发
    NOT_RUN(0, "未跑"),

    //已下发
    RUNNED(1, "已跑"),

    //已下发
    ISSUED(2, "已下发"),

    ALl(3, "已全部生成"),

    NOT_ALL(4, "部分生成");

    private Integer code;

    private String desc;

    CallStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer code() {
        return this.code;
    }
}
