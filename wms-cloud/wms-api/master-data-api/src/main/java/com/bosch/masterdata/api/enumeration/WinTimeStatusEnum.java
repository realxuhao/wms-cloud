package com.bosch.masterdata.api.enumeration;

public enum WinTimeStatusEnum {

    DISABLE("停用", 0),
    ENABL("启用", 1);


    private String desc;
    private Integer code;

    private WinTimeStatusEnum(String desc, Integer code) {
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
