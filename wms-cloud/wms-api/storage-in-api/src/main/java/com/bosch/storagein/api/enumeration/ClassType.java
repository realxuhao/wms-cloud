package com.bosch.storagein.api.enumeration;

public enum ClassType {

    MATERIALRECEIVE("MaterialReceive",0);


    private String desc;
    private Integer code;

    //构造方法
    private ClassType(String desc, Integer code) {
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
