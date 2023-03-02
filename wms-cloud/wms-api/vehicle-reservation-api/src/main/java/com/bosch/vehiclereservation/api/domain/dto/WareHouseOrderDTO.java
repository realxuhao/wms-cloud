package com.bosch.vehiclereservation.api.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class WareHouseOrderDTO {

    private Boolean asc;

    private String condition;

    private Long current;

    private Long limit;

    private Long offset;

    private Long offsetCurrent;

    private Boolean openSort;

    private String orderByField;

    private Long pages;

    private List<RecordDTO> records;

    private Boolean searchCount;

    private Long size;

    private Long total;


}
