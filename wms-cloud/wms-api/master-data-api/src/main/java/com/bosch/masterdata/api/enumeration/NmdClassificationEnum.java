package com.bosch.masterdata.api.enumeration;

public enum NmdClassificationEnum {
    A("Components", 0),
    B("Package", 1);


    private String desc;
    private Integer code;

    //构造方法
    private NmdClassificationEnum(String desc, Integer code) {
        this.desc = desc;
        this.code = code;
    }

    public String getDesc() {
        return this.desc;
    }

    public Integer getCode() {
        return this.code;
    }

    public static boolean contain(Integer desc){
        for (NmdClassificationEnum ele : values()) {
            if (ele.code.equals(desc)) {
                return true;
            }
        }
        return false;
    }
}
