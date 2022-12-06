package com.bosch.binin.api.enumeration;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-11-16 14:17
 **/
public enum KanbanStatusEnum {
    //1 取消任务、2 待下发、3 待下架（下发拣配任务）、外库待发运（下架时PDA扫描SSCC②，外库扣库存）、主库待收货（外库发运扫描，只针对外库的任务），
    // 待上架（针对外库配送主库，一键收货⑥），完成（针对外库配送主库，此时此条任务完成，生成待下架的新任务⑧），
    // 待下架（新任务：针对外库配送主库收货后进行上架而非配送产线），产线待收货（下架扫描之后），产线收货（产线PDA扫描接收）

    //取消任务
    CANCEL(-1,"取消任务"),
    //待下发
    WAITING_ISSUE(0, "待下发"),
    //待下架
    WAITING_BIN_DOWN(1, "待下架"),
    //外库已下架
    OUT_DOWN(2, "外库待发运"),
    //主库已下架
    INNER_DOWN(3, "产线待收货（下架扫描之后）"),
    //主库待收货
    INNER_RECEIVING(4, "主库待收货"),

    //主库上架
    INNER_BIN_IN(5, "待上架"),

    //产线收货
    LINE_RECEIVED(6, "产线已收货"),

    //完成
    FINISH(7, "完成");
    private final Integer value;

    private final String desc;

    KanbanStatusEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer value() {
        return this.value;
    }
    public String getDesc() {
        return this.desc;
    }
    public static String getDesc(String key) {
        for (KanbanStatusEnum ele : values()) {
            if (ele.value.toString().equals(key)) {
                return ele.getDesc();
            }
        }
        return null;
    }

}
