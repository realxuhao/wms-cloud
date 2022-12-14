package com.ruoyi.common.core.enums;

public enum  MoveTypeEnums {
    RECEIVE("0", "收货"),
    STORAGEIN("1", "入库"),
    BININ("2", "上架"),
    CALL("3","生产叫料"),
    WARE_SHIFT("4", "移库"),
    SPLIT_IN("5","拆托上架");

    private final String code;
    private final String info;

    MoveTypeEnums(String code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public String getCode()
    {
        return code;
    }

    public String getInfo()
    {
        return info;
    }
}
