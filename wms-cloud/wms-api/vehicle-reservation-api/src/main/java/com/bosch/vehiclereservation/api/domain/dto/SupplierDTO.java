package com.bosch.vehiclereservation.api.domain.dto;


import lombok.Data;

import java.util.List;

@Data
public class SupplierDTO {

    private String supplierName;

    private SupplierReserveDTO supplierReserveDTO;

    private List<SupplierPorderDTO> supplierPorderDTOS;

}
