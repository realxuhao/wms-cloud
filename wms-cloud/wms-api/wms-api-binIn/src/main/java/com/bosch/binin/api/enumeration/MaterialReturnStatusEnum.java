package com.bosch.binin.api.enumeration;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-11-16 14:17
 **/
public enum MaterialReturnStatusEnum {

    //取消任务
    CANCEL(-1,"取消任务"),
    //待下发
    WAITING_ISSUE(0, "待下发"),
    //待确认
    WAITING_CONFIRM(1, "待确认"),
    //待上架
    WAITING_BIN_IN(2, "待上架"),
    //完成
    FINISH(3, "完成");
    private final Integer value;

    private final String desc;

    MaterialReturnStatusEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer value() {
        return this.value;
    }
    public String getDesc() {
        return this.desc;
    }
    public static String getDesc(String key) {
        for (MaterialReturnStatusEnum ele : values()) {
            if (ele.value.toString().equals(key)) {
                return ele.getDesc();
            }
        }
        return null;
    }

}
