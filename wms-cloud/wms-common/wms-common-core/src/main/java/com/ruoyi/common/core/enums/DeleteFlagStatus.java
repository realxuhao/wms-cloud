package com.ruoyi.common.core.enums;

public enum DeleteFlagStatus {

    FALSE(0, "未删除"), TRUE(1, "删除");

    private final Integer code;
    private final String info;

    DeleteFlagStatus(Integer code, String info)
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
