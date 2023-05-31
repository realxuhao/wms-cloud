package com.bosch.vehiclereservation.api.enumeration;

public enum ReserveStatusEnum {

    RESERVED("已预约", 0),
    ON_ORDER("在途（司机已预约）", 1),
    ARRIVAL("已到货（司机现场签到）", 2),
    COMPLETE("已完成", 3),
    ERROR("异常", 4);

    private String desc;
    private Integer code;

    private ReserveStatusEnum(String desc, Integer code) {
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
