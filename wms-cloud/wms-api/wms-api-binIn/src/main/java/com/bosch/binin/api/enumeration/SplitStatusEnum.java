package com.bosch.binin.api.enumeration;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-11-22 15:09
 **/
public enum SplitStatusEnum {
    //已取消
    WAIT_BIN_IN(0, "待上架"),
    //完成
    FINISH(1, "完成");

    private Integer code;

    private String desc;

    SplitStatusEnum(Integer code, String desc) {
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
        for (SplitStatusEnum ele : values()) {
            if (ele.code.toString().equals(key)) {
                return ele.getDesc();
            }
        }
        return null;
    }

}
