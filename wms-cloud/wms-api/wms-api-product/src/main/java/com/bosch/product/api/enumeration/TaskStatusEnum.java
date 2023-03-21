package com.bosch.product.api.enumeration;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-03-13 10:17
 **/
public enum TaskStatusEnum {

    UNEXECUTED("未执行", 0),
    UNDEREXECUTION("执行中", 1),
    EXECUTED("已执行", 2);

    private String desc;
    private Integer code;

    //构造方法
    private TaskStatusEnum(String desc, Integer code) {
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
        for (TaskStatusEnum ele : values()) {
            if (ele.desc.equals(desc)) {
                return ele.code;
            }
        }
        return Integer.valueOf(0);
    }

    public static String getDescByCode(Integer code) {
        for (TaskStatusEnum ele : values()) {
            if (ele.code.equals(code)) {
                return ele.desc;
            }
        }
        return "";
    }

    public static boolean contain(String desc) {
        for (TaskStatusEnum ele : values()) {
            if (ele.desc.equals(desc)) {
                return true;
            }
        }
        return false;
    }
}
