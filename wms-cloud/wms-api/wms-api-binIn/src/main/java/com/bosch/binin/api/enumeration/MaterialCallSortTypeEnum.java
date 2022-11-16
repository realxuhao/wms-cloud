package com.bosch.binin.api.enumeration;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-11-15 15:12
 **/
public enum MaterialCallSortTypeEnum {
    //BBD优先
    BBD_FIRST(0),
    //基于先主库后外库
    MAIN_WARE_FIRST(1);
    private final Integer value;

    MaterialCallSortTypeEnum(int value) {
        this.value = value;
    }

    public Integer value() {
        return this.value;
    }
}
