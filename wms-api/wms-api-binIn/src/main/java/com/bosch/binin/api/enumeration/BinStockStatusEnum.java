package com.bosch.binin.api.enumeration;

public enum  BinStockStatusEnum {
    //待入库
    PROCESSING(0),
    //已入库
    FINISH(1);
    private final int value;

    BinStockStatusEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
