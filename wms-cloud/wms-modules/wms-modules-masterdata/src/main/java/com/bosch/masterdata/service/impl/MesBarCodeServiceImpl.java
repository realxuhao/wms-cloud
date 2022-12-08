package com.bosch.masterdata.service.impl;

import com.bosch.masterdata.api.domain.vo.MesBarCodeVO;
import com.bosch.masterdata.service.IMesBarCodeService;
import com.ruoyi.common.core.utils.MesBarCodeUtil;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-12-08 17:32
 **/

@Service
public class MesBarCodeServiceImpl implements IMesBarCodeService {

    @Override
    public MesBarCodeVO parseMesBarCode(String mesBarCode) {
        String sscc = MesBarCodeUtil.getSSCC(mesBarCode);
        String batchNb = MesBarCodeUtil.getBatchNb(mesBarCode);
        String quantity = MesBarCodeUtil.getQuantity(mesBarCode);
        String materialNb = MesBarCodeUtil.getMaterialNb(mesBarCode);
        Date expireDate = MesBarCodeUtil.getExpireDate(mesBarCode);
        MesBarCodeVO mesBarCodeVO = new MesBarCodeVO();
        mesBarCodeVO.setBatchNb(batchNb);
        mesBarCodeVO.setExpireDate(expireDate);
        mesBarCodeVO.setMaterialNb(materialNb);
        mesBarCodeVO.setSsccNb(sscc);
        mesBarCodeVO.setQuantity(Double.valueOf(quantity));
        return mesBarCodeVO;
    }


}
