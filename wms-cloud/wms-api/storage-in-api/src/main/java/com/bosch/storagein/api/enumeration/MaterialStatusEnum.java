package com.bosch.storagein.api.enumeration;

public enum MaterialStatusEnum {
    WAIT_IN("待入库", 0),
    IN("已入库", 1);


    private String desc;
    private Integer code;

    //构造方法
    private MaterialStatusEnum(String desc, Integer code) {
        this.desc = desc;
        this.code = code;
    }

    public String getDesc() {
        return this.desc;
    }

    public Integer getCode() {
        return this.code;
    }
}
