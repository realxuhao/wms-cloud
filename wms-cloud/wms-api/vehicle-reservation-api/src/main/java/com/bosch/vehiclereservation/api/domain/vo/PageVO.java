package com.bosch.vehiclereservation.api.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PageVO<T> {

    private List<T> rows;
    private Long total;
}