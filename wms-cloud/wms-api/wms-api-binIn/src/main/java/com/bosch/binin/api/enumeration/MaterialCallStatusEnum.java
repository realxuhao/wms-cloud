package com.bosch.binin.api.enumeration;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-11-22 15:09
 **/
public enum MaterialCallStatusEnum {
    //已取消
    CANCEL(-1, "已取消"),
    //待下发
    WAITING_ISSUE(0, "未下发"),

    //已下发
    PART_ISSUED(1, "部分下发"),

    FULL_ISSUED(2, "全部下发");

    private Integer code;

    private String desc;

    MaterialCallStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer code() {
        return this.code;
    }
}
