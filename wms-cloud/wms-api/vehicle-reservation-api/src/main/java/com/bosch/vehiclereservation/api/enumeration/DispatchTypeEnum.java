package com.bosch.vehiclereservation.api.enumeration;

public enum DispatchTypeEnum {

    DELIVER("送货", 0),
    PICKUP("取货", 1);


    private String desc;
    private Integer code;

    private DispatchTypeEnum(String desc, Integer code) {
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
