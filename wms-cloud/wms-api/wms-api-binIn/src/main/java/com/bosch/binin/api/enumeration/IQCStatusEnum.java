package com.bosch.binin.api.enumeration;

public enum IQCStatusEnum {
    CANCEL(-1, "取消"),
    WAITING_BIN_DOWN(0, "待下架"),

    WAITING_SAMPLE(1, "待抽样"),
    WAITING_BIN_IN(2, "待上架"),

    FINISH(3, "完成"),

    WARE_SHIFTING(4, "移库中");


    private Integer code;

    private String desc;

    IQCStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer code() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }

    public static String getDesc(Integer key) {
        for (IQCStatusEnum ele : values()) {
            if (ele.code==key) {
                return ele.getDesc();
            }
        }
        return null;
    }

}
