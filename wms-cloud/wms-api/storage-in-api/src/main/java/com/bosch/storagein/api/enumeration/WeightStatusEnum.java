package com.bosch.storagein.api.enumeration;

public enum WeightStatusEnum {
    UNUSE(0, "未使用"),
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

    public String getDesc() {
        return this.desc;
    }



}
