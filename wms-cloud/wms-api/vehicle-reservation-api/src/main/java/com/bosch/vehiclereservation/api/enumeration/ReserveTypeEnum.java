package com.bosch.vehiclereservation.api.enumeration;

public enum ReserveTypeEnum {

    NOT_RESERVE("未预约", 0),
    RESERVED("已预约", 1);


    private String desc;
    private Integer code;

    private ReserveTypeEnum(String desc, Integer code) {
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
