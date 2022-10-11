package com.bosch.storagein.api.enumeration;

import org.omg.CORBA.FREE_MEM;

public enum CheckTypeEnum {


    WEIGHT("称重", 0),

    COUNT("数数", 1),

    FREE("免检", 2),

    CHECKED("该批次已检", 3);

    private String desc;
    private Integer code;

    private CheckTypeEnum(String desc, Integer code) {
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
