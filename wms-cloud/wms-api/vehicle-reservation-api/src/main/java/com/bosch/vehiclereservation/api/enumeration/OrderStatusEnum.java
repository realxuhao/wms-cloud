package com.bosch.vehiclereservation.api.enumeration;

public enum OrderStatusEnum {

    NORMAL("正常", 0),
    CLOSE("关闭", 1);

    private String desc;
    private Integer code;

    private OrderStatusEnum(String desc, Integer code) {
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
