package com.bosch.binin.service.impl;

import com.bosch.binin.api.domain.dto.*;
import com.bosch.binin.api.domain.vo.*;
import com.bosch.binin.api.util.MesBarCodeUtil;
import com.bosch.binin.mapper.BinInMapper;
import com.bosch.binin.mapper.BinStockMapper;
import com.bosch.binin.service.IBinAssignmentService;
import com.bosch.masterdata.api.*;
import com.bosch.masterdata.api.RemotePalletService;
import com.bosch.masterdata.api.domain.Pallet;
import com.bosch.masterdata.api.domain.vo.MaterialVO;
import com.bosch.storagein.api.RemoteMaterialInService;
import com.ruoyi.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IBinAssignmentServiceImpl implements IBinAssignmentService {

    @Autowired
    private BinInMapper binInMapper;

    @Autowired
    private BinStockMapper binStockMapper;

    @Autowired
    private RemotePalletService remotePalletService;

    @Autowired
    private RemoteMaterialInService remoteMaterialInService;

    @Autowired
    private RemoteMaterialService remoteMaterialService;
    @Override
    public BinAllocationVO getBinAllocationVO(BinAllocationDTO binAllocationDTO) {
        String mesBarCode = binAllocationDTO.getMesBarCode();
        String sscc = MesBarCodeUtil.getSSCC(mesBarCode);
        String batchNb = MesBarCodeUtil.getBatchNb(mesBarCode);
        String materialNb = MesBarCodeUtil.getMaterialNb(mesBarCode);
        //获取托盘详情
        R<Pallet> byType = remotePalletService.getByType(binAllocationDTO.getPalletType());
        //获取物料详情
        R<MaterialVO> materialVORes = remoteMaterialService.getInfoByMaterialCode(com.bosch.storagein.api.util.MesBarCodeUtil.getMaterialNb(mesBarCode));



        BinAllocationVO binAllocationVO = new BinAllocationVO();
        return binAllocationVO;
    }
}
