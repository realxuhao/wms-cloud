package com.bosch.product.api.enumeration;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-04-17 11:19
 **/
public enum StockTakePlanDetailStatusEnum {
    WAIT_ISSUE("待下发", 0),
    WAIT_TAKE("待盘点", 1),
    WAIT_CONFIRM("待确认", 2),
    FINISH("完成", 3);


    private String desc;
    private Integer code;

    //构造方法
    private StockTakePlanDetailStatusEnum(String desc, Integer code) {
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
        for (StockTakePlanDetailStatusEnum ele : values()) {
            if (ele.desc.equals(desc)) {
                return ele.code;
            }
        }
        return Integer.valueOf(0);
    }

    public static String getDescByCode(Integer code) {
        for (StockTakePlanDetailStatusEnum ele : values()) {
            if (ele.code.equals(code)) {
                return ele.desc;
            }
        }
        return "";
    }

    public static boolean contain(String desc) {
        for (StockTakePlanDetailStatusEnum ele : values()) {
            if (ele.desc.equals(desc)) {
                return true;
            }
        }
        return false;
    }
}
