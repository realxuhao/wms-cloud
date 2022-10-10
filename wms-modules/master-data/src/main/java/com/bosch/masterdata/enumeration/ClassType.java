package com.bosch.masterdata.enumeration;

public enum ClassType {
    MATERIALDTO("MaterialDTO", 0),
    MATERIALBINDTO("MaterialBinDTO", 1),
    SUPPLIERINFODTO("SupplierInfoDTO", 2),
    AREADTO("AreaDTO",3),
    FRAMEDTO("FrameDTO",4),
    BINDTO("BinDTO",5);


    private String desc;
    private Integer code;

    //构造方法
    private ClassType(String desc, Integer code) {
        this.desc = desc;
        this.code = code;
    }

    public String getDesc() {
        return this.desc;
    }

    public Integer getCode() {
        return this.code;
    }
}
