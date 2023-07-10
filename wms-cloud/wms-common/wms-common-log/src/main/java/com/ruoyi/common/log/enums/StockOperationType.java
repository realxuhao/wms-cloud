package com.ruoyi.common.log.enums;

public enum StockOperationType {
    IN(0, "入库"),
    SALESOUT(1, "销售出库"),
    OTHEROUT(2, "其他出库");


    private final Integer code;
    private final String info;

    StockOperationType(Integer code, String info)
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
