package com.ruoyi.common.core.enums;

public enum StatusEnums {
    FALSE(0, "未执行"), TRUE(1, "已执行");

    private final Integer code;
    private final String info;

    StatusEnums(Integer code, String info)
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
