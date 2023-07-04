package com.bosch.product.api.domain.enumeration;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-06-27 10:48
 **/
public enum ProductSPDNPickEnum {

    CANCEL(-1,"已取消"),

    WAITTING_DOWN(0,"待下架"),

    WAITTING_SHIP(1,"待发运"),

    FINISH(2,"已完成");


    private Integer code;

    private String desc;

    ProductSPDNPickEnum(Integer code, String desc) {
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
        for (ProductSPDNPickEnum ele : values()) {
            if (ele.code==key) {
                return ele.getDesc();
            }
        }
        return null;
    }
}
