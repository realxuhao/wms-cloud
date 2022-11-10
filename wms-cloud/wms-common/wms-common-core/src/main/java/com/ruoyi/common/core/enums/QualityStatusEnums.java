package com.ruoyi.common.core.enums;

public enum QualityStatusEnums {
    WAITING_QUALITY("Q", "待质检"),
    USE("U", "可用"),
    BLOCK("B", "锁定");

    private final String code;
    private final String info;

    QualityStatusEnums(String code, String info)
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
