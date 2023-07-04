package com.bosch.product.api.domain.enumeration;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-06-27 10:48
 **/
public enum ProductPickEnum {

    CANCEL(-1,"已取消"),
    WAITING_ISSUE(0,"待下发"),

    WAITTING_DOWN(1,"待下架"),

    WAITTING_SHIP(2,"待发运"),

    FINISH(3,"已完成");


    private Integer code;

    private String desc;

    ProductPickEnum(Integer code, String desc) {
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
        for (ProductPickEnum ele : values()) {
            if (ele.code==key) {
                return ele.getDesc();
            }
        }
        return null;
    }
}
