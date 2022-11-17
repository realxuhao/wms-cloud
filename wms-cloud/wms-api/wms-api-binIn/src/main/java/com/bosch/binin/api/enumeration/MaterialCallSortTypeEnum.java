package com.bosch.binin.api.enumeration;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-11-15 15:12
 **/
public enum MaterialCallSortTypeEnum {
    //BBD优先
    BBD_FIRST(0,"BBD优先"),
    //基于先主库后外库
    MAIN_WARE_FIRST(1,"基于先主库后外库");
    private final Integer value;
    private final String des;
    MaterialCallSortTypeEnum(int value,String des) {
        this.value = value;
        this.des=des;
    }

    public Integer value() {
        return this.value;
    }
    public String des() {
        return this.des;
    }
}
