package com.bosch.product.api.domain.enumeration;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-03-29 14:53
 **/
public enum ProductReceiveEnum {


    WAAITTING_RECEIVE(0,"待入库"),

    RECEIVED(0, "已入库");


    private Integer code;

    private String desc;

    ProductReceiveEnum(Integer code, String desc) {
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
        for (ProductReceiveEnum ele : values()) {
            if (ele.code==key) {
                return ele.getDesc();
            }
        }
        return null;
    }
}
