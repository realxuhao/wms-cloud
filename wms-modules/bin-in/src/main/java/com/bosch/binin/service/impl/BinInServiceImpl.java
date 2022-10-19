package com.bosch.binin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.storagein.api.domain.BinIn;
import com.bosch.storagein.api.domain.Stock;
import com.bosch.storagein.api.domain.dto.BinAllocationDTO;
import com.bosch.storagein.api.domain.dto.BinInQueryDTO;
import com.bosch.storagein.api.domain.vo.BinAllocationVO;
import com.bosch.storagein.api.domain.vo.BinInVO;
import com.bosch.binin.mapper.BinInMapper;
import com.bosch.binin.mapper.BinStockMapper;
import com.bosch.binin.service.IBinInService;
import com.bosch.masterdata.api.domain.Pallet;
import com.bosch.masterdata.api.RemotePalletService;
import com.bosch.storagein.api.RemoteBinInService;
import com.bosch.storagein.api.domain.vo.MaterialInVO;
import com.bosch.storagein.api.util.MesBarCodeUtil;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.utils.bean.BeanConverUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: UWH4SZH
 * @since: 10/19/2022 10:15
 * @description:
 */
@Service
public class BinInServiceImpl extends ServiceImpl<BinInMapper, BinIn> implements IBinInService {


    @Autowired
    private BinInMapper binInMapper;

    @Autowired
    private BinStockMapper binStockMapper;

    @Autowired
    private RemotePalletService remotePalletService;

    @Autowired
    private RemoteBinInService remoteMaterialInService;

    @Override
    public List<BinInVO> selectBinVOList(BinInQueryDTO queryDTO) {
        return binInMapper.selectBinVOList(queryDTO);
    }

    @Override
    public String virtualPalletCode(String palletType) {
        R<Pallet> palletResult = remotePalletService.getByType(palletType);
        if (StringUtils.isNull(palletResult) || StringUtils.isNull(palletResult.getData())) {
            throw new ServiceException("托盘类型：" + palletType + " 不存在");
        }

        if (R.FAIL == palletResult.getCode()) {
            throw new ServiceException(palletResult.getMsg());
        }

        Pallet pallet = palletResult.getData();
        String virtualPrefixCode = pallet.getVirtualPrefixCode();

        return "V-" + virtualPrefixCode + "-" + System.currentTimeMillis();
    }

    @Override
    public BinAllocationVO allocateBinCode(BinAllocationDTO binAllocationDTO) {
        String mesBarCode = binAllocationDTO.getMesBarCode();
        R<MaterialInVO> materialInVOResult = remoteMaterialInService.getByMesBarCode(mesBarCode);
        if (StringUtils.isNull(materialInVOResult) || StringUtils.isNull(materialInVOResult.getData())) {
            throw new ServiceException("该物料：" + binAllocationDTO.getMesBarCode() + " 未入库");
        }

        if (R.FAIL == materialInVOResult.getCode()) {
            throw new ServiceException(materialInVOResult.getMsg());
        }

        MaterialInVO materialInVO = materialInVOResult.getData();

        LambdaQueryWrapper<Stock> stockQueryWrapper = new LambdaQueryWrapper<>();
        stockQueryWrapper.eq(Stock::getId,"1");


        BinIn binIn = BeanConverUtil.conver(binAllocationDTO, BinIn.class);
        binIn.setMaterialNb(materialInVO.getMaterialNb());
        binIn.setBatchNb(MesBarCodeUtil.getBatchNb(mesBarCode));
        binIn.setQuantity(materialInVO.getQuantity());
        return null;
    }
}
