package com.bosch.product.api.domain.enumeration;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-03-29 14:53
 **/
public enum ProductWareShiftEnum {

    CANCEL(-1,"取消"),

    WAITTING_SHIPPING(0,"待发运"),
    WAITTING_RECEIVING(1,"待收货"),
    WAITTING_BIN_IN(2,"待上架"),
    FINISH(3,"完成");


    private Integer code;

    private String desc;

    ProductWareShiftEnum(Integer code, String desc) {
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
        for (ProductWareShiftEnum ele : values()) {
            if (ele.code==key) {
                return ele.getDesc();
            }
        }
        return null;
    }
}
