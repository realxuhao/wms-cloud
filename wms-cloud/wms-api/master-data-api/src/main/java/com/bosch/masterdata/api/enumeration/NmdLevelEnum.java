package com.bosch.masterdata.api.enumeration;

public enum NmdLevelEnum {
    S1("s-1", 1),
    S2("s-2", 2),
    S3("s-3", 3),
    S4("s-4", 4),
    N1("Ⅰ", 1),
    N2("Ⅱ", 2),
    N3("Ⅲ", 3);


    private String desc;
    private Integer code;

    //构造方法
    private NmdLevelEnum(String desc, Integer code) {
        this.desc = desc;
        this.code = code;
    }

    public String getDesc() {
        return this.desc;
    }

    public Integer getCode() {
        return this.code;
    }

    public static boolean contain(String desc){
        for (NmdLevelEnum ele : values()) {
            if (ele.desc.toString().equals(desc)) {
                return true;
            }
        }
        return false;
    }
}
