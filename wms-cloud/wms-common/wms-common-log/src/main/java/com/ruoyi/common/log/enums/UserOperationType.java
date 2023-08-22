package com.ruoyi.common.log.enums;

public enum UserOperationType {
    Import(0, "收货清单导入"),
    MATERIALBININ(1, "原材料上架"),
    BINOUT(2, "原材料拣配下架"),
    BINOUTOTHER(3, "原材料其他下架"),
    PALLETSPLIT(4, "拆托"),
    PALLETTURNOVER(5, "翻托"),
    IQCBINOUT(6, "IQC取样下架"),
    IQCBININ(7, "IQC取样上架"),
    CALLOVER(8, "叫料完成"),
    PRODUCTBININ(9, "成品上架"),

    PRODUCTBINOUT(10, "成品拣配下架"),

    PROBINOUTOTHER(11, "成品其他下架"),

    STORAGEIN(12,"原材料入库"),

    SHIFT_BININ(13,"原材料移库上架"),

    PRODUCT_IMPORT(14,"成品入库导入"),

    PRODUCT_STORAGE_IN(15,"成品入库"),

    PRODUCT_TRANS(16,"成品转储"),

    MATERIAL_TRANS(17,"原材料转储");


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
