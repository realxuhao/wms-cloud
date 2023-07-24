package com.bosch.binin.api.enumeration;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-11-22 15:09
 **/
public enum WareShiftTypeEnum {
    NORMAL(0, "正常移库"),
    IQC(1, "IQC移库"),
    PICK(2, "捡配移库");

    private Integer code;

    private String desc;

    WareShiftTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer code() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }

    public static String getDesc(Integer key) {
        for (WareShiftTypeEnum ele : values()) {
            if (ele.code.toString().equals(key)) {
                return ele.getDesc();
            }
        }
        return null;
    }

}
