package com.ruoyi.common.core.enums;

public enum  MoveTypeEnums {
    RECEIVE("0", "收货"),
    STORAGEIN("1", "入库"),
    BININ("2", "上架");

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
