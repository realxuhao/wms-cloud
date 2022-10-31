package com.bosch.binin.api.enumeration;

public enum BinInStatusEnum {

    //待上架
    PROCESSING(0),
    //已上架
    FINISH(1);
    private final int value;

    BinInStatusEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

}
