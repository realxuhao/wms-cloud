package com.bosch.binin.api.enumeration;

public enum BinInStatusEnum {

    //待上架
    PROCESSING(0),
    //已上架
    FINISH(1),
    //已下架
    DOWN(2);
    private final int value;

    BinInStatusEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

}
