package com.bosch.binin.api.enumeration;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-11-16 14:17
 **/
public enum KanbanStatusEnum {
    //取消任务
    CANCEL(-1,"取消任务"),
    //待下发
    WAIT_ISSUE(0, "待下发"),
    //已下发
    HAS_ISSUED(1, "已下发"),
    //已下架
    HAS_BIN_DOWN(2, "已下架");
    private final Integer value;

    private final String desc;

    KanbanStatusEnum(int value, String desc) {
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
        for (KanbanStatusEnum ele : values()) {
            if (ele.value.toString().equals(key)) {
                return ele.getDesc();
            }
        }
        return null;
    }
}
