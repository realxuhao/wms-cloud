package com.bosch.binin.api.enumeration;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-12-20 14:55
 **/
public enum ManuTransStatusEnum {
    CANCEL(-1, "已取消"),
    WAITING_ISSUE(0, "未下发"),

    //待下架
    WAITING_BIN_DOWN(1, "待下架"),
    WAITING_BIN_IN(2, "待上架"),

    FINISH(3, "完成");



    private Integer code;

    private String desc;

    ManuTransStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public String getDesc() {
        return this.desc;
    }


    public static String getDesc(String key) {
        for (ManuTransStatusEnum ele : values()) {
            if (ele.code.toString().equals(key)) {
                return ele.getDesc();
            }
        }
        return null;
    }

    public Integer code() {
        return this.code;
    }
}
