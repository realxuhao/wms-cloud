package com.bosch.masterdata.service;

import com.bosch.masterdata.api.domain.vo.MesBarCodeVO;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-12-08 17:31
 **/
public interface IMesBarCodeService {
    MesBarCodeVO parseMesBarCode(String mesBarCode);

}
