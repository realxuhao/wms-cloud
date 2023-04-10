package com.bosch.masterdata.api.enumeration;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-03-13 10:17
 **/
public enum AreaTypeEnum {

    RM("原材料", 0),
    PRO("成品", 1),
    IQC("质检", 2),
    UNQUALIFIED("不合格品",3),
    PRO_TEMP("成品暂存区",4);
    

    private String desc;
    private Integer code;

    //构造方法
    private AreaTypeEnum(String desc, Integer code) {
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
        for (AreaTypeEnum ele : values()) {
            if (ele.desc.equals(desc)) {
                return ele.code;
            }
        }
        return Integer.valueOf(0);
    }

    public static String getDescByCode(Integer code) {
        for (AreaTypeEnum ele : values()) {
            if (ele.code.equals(code)) {
                return ele.desc;
            }
        }
        return "";
    }

    public static boolean contain(String desc) {
        for (AreaTypeEnum ele : values()) {
            if (ele.desc.equals(desc)) {
                return true;
            }
        }
        return false;
    }
}
