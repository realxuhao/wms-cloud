package com.bosch.masterdata.api.enumeration;

public enum EcnPlanEnum {
    A("Ⅰ", 1),
    B("Ⅱ", 2),
    C("Ⅲ", 3);


    private String desc;
    private Integer code;

    //构造方法
    private EcnPlanEnum(String desc, Integer code) {
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
        for (EcnPlanEnum ele : values()) {
            if (ele.desc.toString().equals(desc)) {
                return true;
            }
        }
        return false;
    }
}
