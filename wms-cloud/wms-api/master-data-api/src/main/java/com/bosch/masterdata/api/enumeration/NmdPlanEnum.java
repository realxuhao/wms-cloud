package com.bosch.masterdata.api.enumeration;

public enum NmdPlanEnum {
    normal("正常", 1),
    stricter("加严", 2),
    relaxed("放宽", 3);


    private String desc;
    private Integer code;

    //构造方法
    private NmdPlanEnum(String desc, Integer code) {
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
        for (NmdPlanEnum ele : values()) {
            if (ele.code.toString().equals(desc)) {
                return true;
            }
        }
        return false;
    }
}
