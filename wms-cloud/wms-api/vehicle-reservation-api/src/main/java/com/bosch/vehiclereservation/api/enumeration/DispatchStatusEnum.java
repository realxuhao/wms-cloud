package com.bosch.vehiclereservation.api.enumeration;

public enum DispatchStatusEnum {

    WAITE("等待", 0),
    ENTER("进厂", 1),
    COMPLETE("完成", 2);


    private String desc;
    private Integer code;

    private DispatchStatusEnum(String desc, Integer code) {
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
