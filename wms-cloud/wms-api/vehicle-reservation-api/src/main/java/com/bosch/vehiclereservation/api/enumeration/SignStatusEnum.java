package com.bosch.vehiclereservation.api.enumeration;

public enum SignStatusEnum {

    NOT_SIGN("未签到", 0),
    SIGNED("已签到", 1);


    private String desc;
    private Integer code;

    private SignStatusEnum(String desc, Integer code) {
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
