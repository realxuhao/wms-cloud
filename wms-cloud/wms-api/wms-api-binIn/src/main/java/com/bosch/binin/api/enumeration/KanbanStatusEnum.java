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
    WAITING_ISSUE(0, "待下发"),
    //待下架
    WAITING_BIN_DOWN(1, "待下架"),
    //外库已下架
    OUT_DOWN(2, "外库已下架"),
    //主库已下架
    INNER_DOWN(3, "主库已下架"),
    //主库待收货
    INNER_RECEIVING(4, "主库待收货"),
    //主库入库
    INNER_STORAGE_IN(5, "主库入库"),
    //主库上架
    INNER_BIN_IN(6, "主库上架"),
    //产线收货
    LINE_RECEIVING(7, "产线收货");
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
