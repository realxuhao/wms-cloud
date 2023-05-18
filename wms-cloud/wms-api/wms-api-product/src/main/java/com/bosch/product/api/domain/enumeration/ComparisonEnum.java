package com.bosch.product.api.domain.enumeration;

public enum ComparisonEnum {
    DIFF(0,"不同"),

    SAME(1, "相同");


    private Integer code;

    private String desc;

    ComparisonEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer code() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }

    public static String getDesc(Integer key) {
        for (ComparisonEnum ele : values()) {
            if (ele.code==key) {
                return ele.getDesc();
            }
        }
        return null;
    }
}
