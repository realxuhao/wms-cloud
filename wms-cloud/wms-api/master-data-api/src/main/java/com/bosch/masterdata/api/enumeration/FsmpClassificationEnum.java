package com.bosch.masterdata.api.enumeration;

public enum FsmpClassificationEnum {
    A("A", 0),//同一批次的3（N小于3，则为N）个非连续拖中取样（取样量手输入）（塑料瓶、铝膜参考此方式）
    B("B", 1);//同一批次中随机取1个独立包装（随机下架1 SSCC）（取样量手输入）


    private String desc;
    private Integer code;

    //构造方法
    private FsmpClassificationEnum(String desc, Integer code) {
        this.desc = desc;
        this.code = code;
    }

    public String getDesc() {
        return this.desc;
    }

    public Integer getCode() {
        return this.code;
    }

    public static boolean contain(String desc){
        for (FsmpClassificationEnum ele : values()) {
            if (ele.desc.equals(desc)) {
                return true;
            }
        }
        return false;
    }
}
