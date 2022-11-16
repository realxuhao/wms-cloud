package com.bosch.binin.api.enumeration;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-11-16 14:17
 **/
public enum RequirementActionTypeEnum {
    //整托下架
    FULL_BIN_DOWN(0),
    //拆托下架
    PART_BIN_DOWN(1);
    private final Integer value;

    RequirementActionTypeEnum(int value) {
        this.value = value;
    }

    public Integer value() {
        return this.value;
    }
}
