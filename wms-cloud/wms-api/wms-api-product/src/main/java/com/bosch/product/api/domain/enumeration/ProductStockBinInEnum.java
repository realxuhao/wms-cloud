package com.bosch.product.api.domain.enumeration;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-03-29 14:53
 **/
public enum ProductStockBinInEnum {

    NONE(0,"不需要上架"),

    WAITTING_BIN_IN(1,"待上架"),
    FINISH(2,"完成");



    private Integer code;

    private String desc;

    ProductStockBinInEnum(Integer code, String desc) {
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
        for (ProductStockBinInEnum ele : values()) {
            if (ele.code==key) {
                return ele.getDesc();
            }
        }
        return null;
    }
}
