package com.bosch.masterdata.api.enumeration;

public enum EcnClassificationEnum {
    UNTTS("非TTS物料", 0),
    TTS("TTS物料", 1),
    HGG("皇冠盖", 2),
    ZX("国内产品纸箱", 3),
    BLP("玻璃瓶", 4),
    SMS("说明书&标签", 5);


    private String desc;
    private Integer code;

    //构造方法
    private EcnClassificationEnum(String desc, Integer code) {
        this.desc = desc;
        this.code = code;
    }

    public String getDesc() {
        return this.desc;
    }

    public Integer getCode() {
        return this.code;
    }

    public static boolean contain(Integer desc){
        for (EcnClassificationEnum ele : values()) {
            if (ele.desc.equals(desc)) {
                return true;
            }
        }
        return false;
    }
}
