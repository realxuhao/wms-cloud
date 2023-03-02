package com.bosch.vehiclereservation.api.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 采购单查询参数
 */
@Data
public class PoReqDTO {

    /**
     * 每页显示数量：数量 默认10
     */
    private Integer size;

    /**
     * 当前页码 默认1
     */
    private Integer current;

    /**
     * 格式：yyyy-MM-dd
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateDate;

    /**
     * 订单号
     */
    private String poNo;

    /**
     * 订单行号
     */
    private String poItem;

    /**
     * 物料代号
     */
    private String sapCode;

    /**
     * 发票号
     */
    private String invoiceNo;

}
