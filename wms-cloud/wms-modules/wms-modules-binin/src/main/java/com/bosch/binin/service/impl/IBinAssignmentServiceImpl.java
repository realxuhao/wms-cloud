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
import com.bosch.masterdata.api.domain.Bin;
import com.bosch.masterdata.api.domain.Pallet;
import com.bosch.masterdata.api.domain.vo.BinVO;
import com.bosch.masterdata.api.domain.vo.FrameVO;
import com.bosch.masterdata.api.domain.vo.MaterialBinVO;
import com.bosch.masterdata.api.domain.vo.MaterialVO;
import com.bosch.storagein.api.RemoteMaterialInService;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.MesBarCodeUtil;
import com.ruoyi.common.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    @Transactional(rollbackFor = Exception.class)
    public BinAllocationVO getBinAllocationVO(BinAllocationDTO binAllocationDTO) {


        
        String mesBarCode = binAllocationDTO.getMesBarCode();
        String sscc = MesBarCodeUtil.getSSCC(mesBarCode);
        String batchNb = MesBarCodeUtil.getBatchNb(mesBarCode);
        String materialNb = MesBarCodeUtil.getMaterialNb(mesBarCode);
        //获取托盘详情
        R<Pallet> pallet = remotePalletService.getByType(binAllocationDTO.getPalletType());
        //获取物料详情
        R<MaterialVO> materialVORes =
                remoteMaterialService.getInfoByMaterialCode(MesBarCodeUtil.getMaterialNb(mesBarCode));
        //可用库位
        List<MaterialBinVO> materialBinVOList=new ArrayList<>();

        List<String> frameTypeCodes=new ArrayList<>();
        //根据物料号 获取物料跨关系
        R<List<MaterialBinVO>> listByMaterial = remoteMasterDataService.getListByMaterial(materialNb);
        if (listByMaterial.isSuccess()){
            if(listByMaterial.getData()==null){
                throw new ServiceException("该物料号未维护可用跨") ;
            }
            materialBinVOList=listByMaterial.getData().stream().
                    sorted(Comparator.comparing(MaterialBinVO::getPriorityLevel, Comparator.naturalOrder())).
                    collect(Collectors.toList());
            frameTypeCodes=materialBinVOList.stream().map(MaterialBinVO::getFrameTypeCode).collect(Collectors.toList());
            Collections.sort(frameTypeCodes);
        }else {
            throw new ServiceException("获取物料相关跨失败") ;
        }


        //获取同批次 同物料号 已占用跨 、库位
        LambdaQueryWrapper<BinIn> binInWrapper = new LambdaQueryWrapper();
        binInWrapper.eq(BinIn::getBatchNb, batchNb);
        binInWrapper.eq(BinIn::getSsccNumber, sscc);
        binInWrapper.eq(BinIn::getStatus, BinInStatusEnum.PROCESSING).or().eq(BinIn::getStatus, BinInStatusEnum.FINISH);
        List<BinIn> binIns = binInMapper.selectList(binInWrapper);

        BinAllocationVO binAllocationVO=new BinAllocationVO();
        binAllocationVO.setMesBarCode(mesBarCode);
        binAllocationVO.setSsccNumber(sscc);
        binAllocationVO.setMaterialNb(materialNb);
        binAllocationVO.setPalletType(binAllocationDTO.getPalletType());
        binAllocationVO.setPalletCode(binAllocationDTO.getPalletCode());
        binAllocationVO.setWareCode("");

        //存在同批次数据
        if (CollectionUtils.isNotEmpty(binIns)) {
            //1.判断同跨能否放入 （可用宽度重量，上架状态） 按照跨从小到大排序
            List<String> usedFrameCodes = new ArrayList<>();
            //实际跨id
            List<BinIn> actualBins =
                    binIns.stream().filter(r -> r.getActualFrameId()!=null).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(actualBins)) {
                List<String> collect = actualBins.stream().map(BinIn::getActualFrameCode).collect(Collectors.toList());
                boolean a = CollectionUtils.isNotEmpty(collect) ? usedFrameCodes.addAll(collect) : null;
            }
            //推荐跨
            List<BinIn> recommendBins =
                    binIns.stream().filter(r -> r.getActualFrameId()!=null && r.getRecommendBinCode()!=null).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(recommendBins)) {
                List<String> collect =
                        recommendBins.stream().map(BinIn::getRecommendFrameCode).collect(Collectors.toList());
                boolean b = CollectionUtils.isNotEmpty(collect) ? usedFrameCodes.addAll(collect) : null;
            }
            //占用库位排序
            Collections.sort(usedFrameCodes);
            // 2.能放入 查找该跨上已占用库位、剩余库位，3.获取最小库位
            for (String usedCodes : usedFrameCodes) {
                FrameRemainVO frameRemainVO = validateBin(usedCodes, materialVORes.getData(), pallet.getData());
                if (frameRemainVO!=null){
                    binAllocationVO.setRecommendBinCode(frameRemainVO.getRecommendBinCode());
                }
            }

        } else {
            //根据可用宽度重量 查找最近能放入的跨

            //查找该跨上已占用库位，剩余库位

            //获取最小库位
        }


        //根据跨查找库位
        
        return binAllocationVO;
    }


    /**
     * 判断是否能放入跨
     *
     * @param frameCode
     * @param material
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public FrameRemainVO validateBin(String frameCode, MaterialVO material, Pallet pallet) {
        try {
            FrameRemainVO frameRemainVO=new FrameRemainVO();
            //获取来料每托重量
            BigDecimal totalWeight = material.getTotalWeight();
            //获取托盘宽度
            BigDecimal width = pallet.getWidth();
            //获取bin关联跨，再获取跨承重，宽度
            R<FrameVO> frameInfoByCode = remoteMasterDataService.getFrameInfoByCode(frameCode);
            //R<BinVO> binResult = remoteMasterDataService.getBinInfoByCode(binCode);
            if (frameInfoByCode.isSuccess()) {
                FrameVO frameData = frameInfoByCode.getData();
                if (frameData != null) {
                    //承重
                    BigDecimal frameBearWeight = frameData.getBearWeight();
                    //宽度
                    BigDecimal frameWidth = frameData.getWidth();
                    //获取跨上剩余承重，宽度
                     frameRemainVO = getBins(frameData.getId());

                    if (frameBearWeight.subtract(frameRemainVO.getFrameBearWeight()).compareTo(totalWeight)<0 ){
                        return null;
                    }
                    if (frameWidth.subtract(frameRemainVO.getFrameWidth()).compareTo(width)<0 ){
                        return null;
                    }
                    return frameRemainVO;
                }
                return null;
            } else {
                return null;
            }
            //获取跨上剩余承重

        } catch (Exception e) {
            throw new ServiceException("判断是否能放入跨出错");

        }
    }


    /**
     * 获取跨上占用
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public FrameRemainVO getBins (Long frameId) {
        try {
            List<BinIn> list=new ArrayList<>();
            List<String> usedBins=new ArrayList<>();
            //已上架 查询实际库位
            LambdaQueryWrapper<BinIn> queryActual = new LambdaQueryWrapper<>();
            queryActual.eq(BinIn::getActualFrameId,frameId);
            queryActual.eq(BinIn::getStatus,BinInStatusEnum.FINISH);
            List<BinIn> actualBins = binInMapper.selectList(queryActual);
            if(CollectionUtils.isNotEmpty(actualBins) ){
                list.addAll(actualBins);
                //获取已使用库位
                usedBins.addAll(actualBins.stream().map(BinIn::getActualBinCode).collect(Collectors.toList()));
            }
            //未上架 查询分配库位
            LambdaQueryWrapper<BinIn> queryRecommend = new LambdaQueryWrapper<>();
            queryRecommend.eq(BinIn::getRecommendFrameId,frameId);
            queryRecommend.eq(BinIn::getStatus,BinInStatusEnum.PROCESSING);
            List<BinIn> recommendBins = binInMapper.selectList(queryActual);
            if(CollectionUtils.isNotEmpty(recommendBins) ){
                list.addAll(recommendBins);
                //获取已使用库位
                usedBins.addAll(actualBins.stream().map(BinIn::getRecommendBinCode).collect(Collectors.toList()));
            }

            //获取承重宽度
            FrameRemainVO frameRemain = getFrameRemain(list);
            //先分配跨上最小库位
            R<List<Bin>> infoByFrameId = remoteMasterDataService.getInfoByFrameId(frameId);
            if (infoByFrameId.isSuccess()){
                List<Bin> data = infoByFrameId.getData();
                List<String> canUseBins = data.stream().map(Bin::getCode).collect(Collectors.toList());
                //自然排序
                Collections.sort(canUseBins);

                canUseBins.forEach(r->{
                    if (!usedBins.contains(r)){
                        frameRemain.setRecommendBinCode(r);
                    }

                });

            }else {
                throw  new ServiceException("根据frameid获取最小库位失败");
            }



            return frameRemain;
        } catch (Exception e) {
            throw new ServiceException("");
        }

    }

    /**
     * 获取跨上承重
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public FrameRemainVO getFrameRemain( List<BinIn> actualBins) {
        try {
            FrameRemainVO frameRemainVO = new FrameRemainVO();
            if (CollectionUtils.isNotEmpty(actualBins)){
                for (BinIn actualBin : actualBins) {
                    //获取托盘详情
                    R<Pallet> byType = remotePalletService.getByType(actualBin.getPalletType());
                    if (byType.isSuccess()) {
                        Pallet data = byType.getData();
                        frameRemainVO.setFrameWidth(frameRemainVO.getFrameBearWeight().add(data.getWidth()));

                    } else {
                        throw  new ServiceException("未获取到托盘详情");
                    }
                    //获取物料详情
                    R<MaterialVO> materialVORes =
                            remoteMaterialService.getInfoByMaterialCode(actualBin.getMaterialNb());
                    if (materialVORes.isSuccess()) {
                        MaterialVO data = materialVORes.getData();
                        frameRemainVO.setFrameBearWeight(frameRemainVO.getFrameBearWeight().add(data.getTotalWeight()));
                    } else {
                        throw  new ServiceException("未获取到物料详情");
                    }
                }
            }

            return frameRemainVO;
        } catch (Exception e) {
            throw new ServiceException("获取跨上承重失败");
        }

    }
}
