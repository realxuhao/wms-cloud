package com.bosch.vehiclereservation.api.enumeration;

public enum LateEnum {

    NOT_LATE("未迟到", 0),
    LATE("迟到", 1);


    private String desc;
    private Integer code;

    private LateEnum(String desc, Integer code) {
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
