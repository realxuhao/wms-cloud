package com.bosch.binin.api.domain.vo;

import com.bosch.binin.api.domain.WareShift;
import lombok.Data;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-12-07 14:32
 **/
@Data
public class WareShiftVO extends WareShift {

    private String recommendBinCode;
    private String materialName;


}
