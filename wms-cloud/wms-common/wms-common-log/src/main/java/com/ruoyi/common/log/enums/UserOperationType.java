package com.ruoyi.common.log.enums;

public enum UserOperationType {
    BININ(1, "原材料上架"),
    BINOUT(2, "拣配下架"),
    BINOUTOTHER(3, "其他下架"),
    PALLETSPLIT(4, "拆托"),
    PALLETTURNOVER(5, "翻托");

    private final Integer code;
    private final String info;

    UserOperationType(Integer code, String info)
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
