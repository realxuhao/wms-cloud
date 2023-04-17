package com.bosch.product.api.enumeration;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-04-17 11:19
 **/
public enum StockTakePlanStatusEnum {
    CREATED("已创建", 0),
    PROCESSING("进行中", 1),
    FINISH("已完成", 2);

    private String desc;
    private Integer code;

    //构造方法
    private StockTakePlanStatusEnum(String desc, Integer code) {
        this.desc = desc;
        this.code = code;
    }

    public String getDesc() {
        return this.desc;
    }

    public Integer getCode() {
        return this.code;
    }

    public static Integer getCodeByDesc(String desc) {
        for (StockTakePlanStatusEnum ele : values()) {
            if (ele.desc.equals(desc)) {
                return ele.code;
            }
        }
        return Integer.valueOf(0);
    }

    public static String getDescByCode(Integer code) {
        for (StockTakePlanStatusEnum ele : values()) {
            if (ele.code.equals(code)) {
                return ele.desc;
            }
        }
        return "";
    }

    public static boolean contain(String desc) {
        for (StockTakePlanStatusEnum ele : values()) {
            if (ele.desc.equals(desc)) {
                return true;
            }
        }
        return false;
    }
}
