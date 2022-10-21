package com.bosch.binin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.bosch.binin.api.domain.BinIn;
import com.bosch.binin.api.domain.dto.*;
import com.bosch.binin.api.domain.vo.*;

import com.bosch.binin.api.enumeration.BinInStatusEnum;
import com.bosch.binin.mapper.BinInMapper;
import com.bosch.binin.mapper.BinStockMapper;
import com.bosch.binin.service.IBinAssignmentService;
import com.bosch.masterdata.api.*;
import com.bosch.masterdata.api.RemotePalletService;
import com.bosch.masterdata.api.domain.Pallet;
import com.bosch.masterdata.api.domain.vo.BinVO;
import com.bosch.masterdata.api.domain.vo.MaterialBinVO;
import com.bosch.masterdata.api.domain.vo.MaterialVO;
import com.bosch.storagein.api.RemoteMaterialInService;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.MesBarCodeUtil;
import com.ruoyi.common.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private RemoteMasterDataService remoteMasterDataService;
    @Override
    public BinAllocationVO getBinAllocationVO(BinAllocationDTO binAllocationDTO) {
        String mesBarCode = binAllocationDTO.getMesBarCode();
        String sscc = MesBarCodeUtil.getSSCC(mesBarCode);
        String batchNb = MesBarCodeUtil.getBatchNb(mesBarCode);
        String materialNb = MesBarCodeUtil.getMaterialNb(mesBarCode);
        //获取托盘详情
        R<Pallet> byType = remotePalletService.getByType(binAllocationDTO.getPalletType());
        //获取物料详情
        R<MaterialVO> materialVORes = remoteMaterialService.getInfoByMaterialCode(MesBarCodeUtil.getMaterialNb(mesBarCode));



        R<BinVO> binInfo = remoteMasterDataService.getBinInfo(1L);
        //获取物料相关跨
        R<List<MaterialBinVO>> listByMaterial = remoteMasterDataService.getListByMaterial(materialNb);
        //获取同批次 同物料号 已占用跨 、库位
        LambdaQueryWrapper<BinIn> binInWrapper=new LambdaQueryWrapper();
        binInWrapper.eq(BinIn::getBatchNb,batchNb);
        binInWrapper.eq(BinIn::getSsccNumber,sscc);
        binInWrapper.eq(BinIn::getStatus, BinInStatusEnum.PROCESSING).or().eq(BinIn::getStatus, BinInStatusEnum.FINISH);
        List<BinIn> binIns = binInMapper.selectList(binInWrapper);
        //存在同批次数据
        if (CollectionUtils.isNotEmpty(binIns)){
            //1.判断同跨能否放入 （可用宽度重量，上架状态） 按照跨从小到大排序
            List<BinIn> collect =
                    binIns.stream().filter(r -> StringUtils.isEmpty(r.getActualBinCode()) ).collect(Collectors.toList());

            // 2.能放入 查找该跨上已占用库位、剩余库位，3.获取最小库位

        }else {
            //根据可用宽度重量 查找最近能放入的跨

            //查找该跨上已占用库位，剩余库位

            //获取最小库位
        }



        //根据跨查找库位

        BinAllocationVO binAllocationVO = new BinAllocationVO();
        return binAllocationVO;
    }
}
