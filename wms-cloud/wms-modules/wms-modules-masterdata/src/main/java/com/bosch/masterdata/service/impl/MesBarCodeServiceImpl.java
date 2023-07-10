package com.bosch.masterdata.service.impl;

import com.bosch.masterdata.api.domain.vo.MaterialVO;
import com.bosch.masterdata.api.domain.vo.MesBarCodeVO;
import com.bosch.masterdata.service.IMaterialService;
import com.bosch.masterdata.service.IMesBarCodeService;
import com.ruoyi.common.core.utils.MesBarCodeUtil;
import com.ruoyi.common.core.utils.ProductQRCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private IMaterialService materialService;

    @Override
    public MesBarCodeVO parseMesBarCode(String mesBarCode) {
        MesBarCodeVO mesBarCodeVO = new MesBarCodeVO();
        if (mesBarCode.length()<=50) {
            String sscc = MesBarCodeUtil.getSSCC(mesBarCode);
            String batchNb = MesBarCodeUtil.getBatchNb(mesBarCode);
            String quantity = MesBarCodeUtil.getQuantity(mesBarCode);
            String materialNb = MesBarCodeUtil.getMaterialNb(mesBarCode);
            Date expireDate = MesBarCodeUtil.getExpireDate(mesBarCode);
            mesBarCodeVO.setBatchNb(batchNb);
            mesBarCodeVO.setExpireDate(expireDate);
            mesBarCodeVO.setMaterialNb(materialNb);
            mesBarCodeVO.setSsccNb(sscc);
            mesBarCodeVO.setQuantity(Double.valueOf(quantity));
            MaterialVO materialVO = materialService.selectMaterialVOBymaterialCode(materialNb);
//        mesBarCodeVO.setMaterialVO(materialVO);
            mesBarCodeVO.setMaterialName(materialVO.getName());
            mesBarCodeVO.setUnit(materialVO.getUnit());
        }else {

            mesBarCodeVO.setSsccNb(ProductQRCodeUtil.getSSCC(mesBarCode));
            mesBarCodeVO.setProductionDate(ProductQRCodeUtil.getProductionDate(mesBarCode));
            mesBarCodeVO.setBatchNb(ProductQRCodeUtil.getBatchNb(mesBarCode));
        }
        return mesBarCodeVO;
    }


}
