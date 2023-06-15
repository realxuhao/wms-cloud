package com.bosch.binin.api.enumeration;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-06-12 13:59
 **/
public enum SPDNStatusEnum {
    //正常
    WAITING_APPROVE(0, "待审批"),

    //异常
    APPROVED(1, "已审批");


    private Integer code;

    private String desc;

    SPDNStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer code() {
        return this.code;
    }
}
