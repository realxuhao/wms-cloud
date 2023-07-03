package com.bosch.product.api.domain.enumeration;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-03-29 14:53
 **/
public enum SUDNStatusEnum {

    CANCEL(-1,"已取消"),

    WAITING_GEN(0,"待生成"),

    GENERATED(1,"已生成");



    private Integer code;

    private String desc;

    SUDNStatusEnum(Integer code, String desc) {
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
        for (SUDNStatusEnum ele : values()) {
            if (ele.code==key) {
                return ele.getDesc();
            }
        }
        return null;
    }
}
