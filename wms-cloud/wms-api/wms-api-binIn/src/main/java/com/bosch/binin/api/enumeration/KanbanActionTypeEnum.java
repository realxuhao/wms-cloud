package com.bosch.binin.api.enumeration;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-11-16 14:17
 **/
public enum KanbanActionTypeEnum {
    //整托下架
    FULL_BIN_DOWN(0,"整托下架"),
    //拆托下架
    PART_BIN_DOWN(1,"拆托下架");
    private final Integer value;
    private final String desc;
    KanbanActionTypeEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer value() {
        return this.value;
    }
    public String getDesc() {
        return this.desc;
    }
    public static String getDesc(Integer key) {
        for (KanbanActionTypeEnum ele : values()) {
            if (ele.value==key) {
                return ele.getDesc();
            }
        }
        return null;
    }

}
