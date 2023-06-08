package com.bosch.masterdata.api.domain;

import lombok.Data;

@Data
public class MissionToDo {
    //待入库数量
    private Integer toBeReceived;
    //上架数量
    private Integer toBeBin;
    //叫料数量
    private Integer toBeCall;
    //移库数量
    private Integer toBeMove;
    //仓库code
    private String wareCode;
    //cell
    private String cell;


}
