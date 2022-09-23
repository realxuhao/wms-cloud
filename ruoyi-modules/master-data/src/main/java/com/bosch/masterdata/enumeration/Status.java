package com.bosch.masterdata.enumeration;

public enum Status {

    DISABLE("停用", 0),
    ENABLE("启用", 1);


    private String desc;
    private Integer code;

    //构造方法
    private Status(String desc, Integer code) {
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
