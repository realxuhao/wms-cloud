package com.ruoyi.common.log.enums;

public enum MaterialType {
    MATERIAL(0, "物料"),
    PRODUCT(1, "成品");


    private final Integer code;
    private final String info;

    MaterialType(Integer code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public Integer getCode()
    {
        return code;
    }

    public String getInfo()
    {
        return info;
    }
}
