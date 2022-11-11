package com.bosch.binin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.bosch.binin.api.domain.BinIn;
import com.bosch.binin.api.domain.dto.*;
import com.bosch.binin.api.domain.vo.*;

import com.bosch.binin.api.enumeration.BinInStatusEnum;
import com.bosch.binin.mapper.BinInMapper;
import com.bosch.binin.mapper.StockMapper;
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
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.MesBarCodeUtil;
import com.ruoyi.common.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class IBinAssignmentServiceImpl implements IBinAssignmentService {

    @Autowired
    private BinInMapper binInMapper;

    @Autowired
    private StockMapper stockMapper;

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
        BinAllocationVO binAllocationVO = new BinAllocationVO();
        binAllocationVO.setMesBarCode(mesBarCode);
        binAllocationVO.setSsccNumber(sscc);
        binAllocationVO.setMaterialNb(materialNb);
        binAllocationVO.setPalletType(binAllocationDTO.getPalletType());
        binAllocationVO.setPalletCode(binAllocationDTO.getPalletCode());
        binAllocationVO.setWareCode("");

        //获取同批次 同物料号 已占用跨 、库位
        LambdaQueryWrapper<BinIn> binInWrapper = new LambdaQueryWrapper();
        binInWrapper.eq(BinIn::getBatchNb, batchNb);
        binInWrapper.eq(BinIn::getSsccNumber, sscc);
        binInWrapper.eq(BinIn::getMaterialNb, materialNb);
        binInWrapper.eq(BinIn::getStatus, BinInStatusEnum.PROCESSING).or().eq(BinIn::getStatus, BinInStatusEnum.FINISH);
        List<BinIn> binIns = binInMapper.selectList(binInWrapper);

        //已占库位和跨的map
        Map<String, String> usedBins = getUsedBins(binIns);
        //获取托盘详情
        R<Pallet> palletR = remotePalletService.getByType(binAllocationDTO.getPalletType());
        if (!palletR.isSuccess()){
            throw new ServiceException("获取托盘详情失败");
        }
        Pallet pallet = palletR.getData();
        if(pallet==null){
            throw new ServiceException("未获取到托盘数据");
        }
        //获取物料详情
        R<MaterialVO> materialVORes =
                remoteMaterialService.getInfoByMaterialCode(MesBarCodeUtil.getMaterialNb(mesBarCode));
        if(!materialVORes.isSuccess()){
            throw new ServiceException("获取物料详情失败");
        }
        MaterialVO materVO = materialVORes.getData();
        if(materVO==null){
            throw new ServiceException("未获取到物料详情");
        }
        //可用库位
        List<MaterialBinVO> materialBinVOList = new ArrayList<>();

        List<String> frameTypeCodes = new ArrayList<>();
        //根据物料号 登陆人仓库 获取物料跨关系
        R<List<MaterialBinVO>> listByMaterial = remoteMasterDataService.getListByMaterial(materialNb);
        if (!listByMaterial.isSuccess()) {
            throw new ServiceException("获取物料相关跨失败");
        }
        if (CollectionUtils.isEmpty(listByMaterial.getData())) {
            throw new ServiceException("该物料号未维护可用跨");
        }
            //跨类型根据优先级排序
            materialBinVOList = listByMaterial.getData().stream().
                    sorted(Comparator.comparing(MaterialBinVO::getPriorityLevel, Comparator.naturalOrder())).
                    collect(Collectors.toList());
            frameTypeCodes =
                    materialBinVOList.stream().map(MaterialBinVO::getFrameTypeCode).collect(Collectors.toList());
            //根据跨类型获取跨list
            for (String frameTypeCode : frameTypeCodes) {
                R<List<FrameVO>> framesListResult = remoteMasterDataService.getFrameInfoByType(frameTypeCode);
                if (!framesListResult.isSuccess()) {
                    throw new ServiceException("根据跨类型获取跨失败");
                }

                List<FrameVO> framesVOByType = framesListResult.getData();
                if (CollectionUtils.isEmpty(framesVOByType)) {
                    //跨类型获取到为空
                    continue;
                }
                //获取所有跨编码
                List<String> framesByType = framesVOByType.stream().map(FrameVO::getCode).collect(Collectors.toList());
                //查看list中 该物料是否有占用库位
                //根据跨类型获取所有库位
                R<List<BinVO>> listR = remoteMasterDataService.selectBinVOByFrameType(frameTypeCode);
                if (!listR.isSuccess()) {
                    throw new ServiceException("根据跨类型获取所有库位失败");
                }
                List<BinVO> binVOs = listR.getData();
                if (CollectionUtils.isEmpty(binVOs)) {
                    //跨类型获取库位为空
                    continue;
                }
                //跨下所有库位编码
                List<String> allBins = binVOs.stream().map(BinVO::getCode).collect(Collectors.toList());
                //判断是否有占用库位 usedBins allBins
                //有占用库位
                if (usedBins.size()>0){
                    boolean disjoint = Collections.disjoint(usedBins.keySet(), allBins);
                    //没有交集，占用库位不在该跨类型中
                    if (disjoint) {
                        //查询下一个跨类型
                        continue;
                    } else {
                        //todo 跨排序
                        List<String> usedFrames = usedBins.values().stream().distinct().collect(Collectors.toList());
                        List<String> usedSortFrames =sortList(usedFrames);
                        //根据排完序的库位，在库位list中查到当前占用跨，判断跨能否放入
                        String canUseCode = getCanUseCode(framesByType, usedSortFrames, materVO, pallet);
                        if(StringUtils.isNotEmpty(canUseCode)){
                            binAllocationVO.setRecommendBinCode(canUseCode);
                            return  binAllocationVO;
                        }else {
                            continue;
                        }
                    }
                }else {
                    //未占用库位
                    //根据排完序的库位，在库位list中查到当前占用跨，判断跨能否放入
                    String canUseCode = getCanUseCode(framesByType, null, materVO, pallet);
                    if(StringUtils.isNotEmpty(canUseCode)){
                        binAllocationVO.setRecommendBinCode(canUseCode);
                        return  binAllocationVO;
                    }else {
                        continue;
                    }
                }
            }
        throw new ServiceException("根据跨类型在主数据中未找到跨的数据");
//        return binAllocationVO;
    }

    public List<String> sortList(List<String> frames){
        List<String> changes = new ArrayList<>();
        List<String> result = new ArrayList<>();
        frames.forEach(r->{
            String[] splitr = r.split("\\.");
            if(splitr.length<3){
                throw  new ServiceException(r+"跨位编码有误，请去维护");
            }
            changes.add(splitr[0]+"."+splitr[2]+"."+splitr[1]);
        });
       Collections.sort(changes);
        changes.forEach(r->{
            String[] splitr = r.split("\\.");
            result.add(splitr[0]+"."+splitr[2]+"."+splitr[1]);

        });
        return  result;
    }

    /**
     * 获取可用库位
     *
     * @param framesUnSorted
     * @param framesUsed
     * @return
     */
    public String getCanUseCode(List<String> framesUnSorted, List<String> framesUsed, MaterialVO material,
                                Pallet pallet) {

        if (CollectionUtils.isEmpty(framesUnSorted)) {
            return null;
        }
        List<String> framesByType = sortList(framesUnSorted);
        //未占用库位
        if(CollectionUtils.isEmpty(framesUsed)){
            //list 要去重 排序
                for (int moveFlag = 0; moveFlag < framesByType.size(); moveFlag++) {
                    //取下一个
                    if (moveFlag < framesByType.size()) {
                        FrameRemainVO frameRemainVO = validateBin(framesByType.get(moveFlag), material, pallet);
                        //返回库位
                        if (frameRemainVO != null) {
                            return frameRemainVO.getRecommendBinCode();
                        }
                    }
                }
        }else {
            //占用库位
            //list 要去重 排序
            for (String frame : framesUsed) {
                if (!framesByType.contains(frame)) {
                    continue;
                }
                int usedIndex = framesByType.indexOf(frame);


                for (int moveFlag = 0; moveFlag < framesByType.size(); moveFlag++) {
                    //取上一个
                    if ((usedIndex - moveFlag) >= 0) {
                        FrameRemainVO frameRemainVO = validateBin(framesByType.get(usedIndex - moveFlag), material, pallet);
                        //返回库位
                        if (frameRemainVO != null) {
                            return frameRemainVO.getRecommendBinCode();
                        }

                    }
                    //取下一个
                    if ((usedIndex + moveFlag) < framesByType.size()) {
                        FrameRemainVO frameRemainVO = validateBin(framesByType.get(usedIndex + moveFlag), material, pallet);
                        //返回库位
                        if (frameRemainVO != null) {
                            return frameRemainVO.getRecommendBinCode();
                        }
                    }
                }

            }
        }

        return null;
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
            FrameRemainVO frameRemainVO = new FrameRemainVO();
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
                    frameRemainVO = getBins(frameData.getId(),frameCode,pallet);
                    if (frameBearWeight.subtract(frameRemainVO.getFrameBearWeight()).compareTo(totalWeight) < 0) {
                        return null;
                    }
                    if (frameWidth.subtract(frameRemainVO.getFrameWidth()).compareTo(width) < 0) {
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
            throw new ServiceException(e.getMessage());
        }
    }


    /**
     * 获取跨上占用
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public FrameRemainVO getBins(Long frameId,String frameCode,Pallet pallet) {
        try {
            List<BinIn> list = new ArrayList<>();
            List<String> usedBins = new ArrayList<>();
            //已上架 查询实际库位
            LambdaQueryWrapper<BinIn> queryActual = new LambdaQueryWrapper<>();
            queryActual.eq(BinIn::getActualFrameId, frameId);
            queryActual.eq(BinIn::getStatus, BinInStatusEnum.FINISH);
            List<BinIn> actualBins = binInMapper.selectList(queryActual);
            if (CollectionUtils.isNotEmpty(actualBins)) {
                list.addAll(actualBins);
                //获取已使用库位
                usedBins.addAll(actualBins.stream().map(BinIn::getActualBinCode).collect(Collectors.toList()));
            }
            //未上架 查询分配库位
            LambdaQueryWrapper<BinIn> queryRecommend = new LambdaQueryWrapper<>();
            queryRecommend.eq(BinIn::getRecommendFrameId, frameId);
            queryRecommend.eq(BinIn::getStatus, BinInStatusEnum.PROCESSING);
            List<BinIn> recommendBins = binInMapper.selectList(queryActual);
            if (CollectionUtils.isNotEmpty(recommendBins)) {
                list.addAll(recommendBins);
                //获取已使用库位
                usedBins.addAll(actualBins.stream().map(BinIn::getRecommendBinCode).collect(Collectors.toList()));
            }

            //获取承重宽度
            FrameRemainVO frameRemain = getFrameRemain(list);
            //先分配跨上最小库位
            R<List<Bin>> infoByFrameId = remoteMasterDataService.getInfoByFrameId(frameId);
            if (infoByFrameId.isSuccess()) {
                List<Bin> data = infoByFrameId.getData();
                if(CollectionUtils.isEmpty(data)){
                    throw new ServiceException(frameCode+"跨上未获取到库位");
                }
                List<String> canUseBins = data.stream().map(Bin::getCode).collect(Collectors.toList());
                //自然排序
                Collections.sort(canUseBins);

                for (String r : canUseBins) {
                    if (!usedBins.contains(r)) {
                        frameRemain.setRecommendBinCode(r);
                        return frameRemain;
                    }
                }

            } else {
                throw new ServiceException("根据frameid获取最小库位失败");
            }


            return frameRemain;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }

    }

    /**
     * 获取跨上承重
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public FrameRemainVO getFrameRemain(List<BinIn> actualBins) {
        try {
            FrameRemainVO frameRemainVO = new FrameRemainVO();
            if (CollectionUtils.isNotEmpty(actualBins)) {
                for (BinIn actualBin : actualBins) {
                    //获取托盘详情
                    R<Pallet> byType = remotePalletService.getByType(actualBin.getPalletType());
                    if (byType.isSuccess()) {
                        Pallet data = byType.getData();
                        frameRemainVO.setFrameWidth(frameRemainVO.getFrameBearWeight().add(data.getWidth()));

                    } else {
                        throw new ServiceException("未获取到托盘详情");
                    }
                    //获取物料详情
                    R<MaterialVO> materialVORes =
                            remoteMaterialService.getInfoByMaterialCode(actualBin.getMaterialNb());
                    if (materialVORes.isSuccess()) {
                        MaterialVO data = materialVORes.getData();
                        frameRemainVO.setFrameBearWeight(frameRemainVO.getFrameBearWeight().add(data.getTotalWeight()));
                    } else {
                        throw new ServiceException("未获取到物料详情");
                    }
                }
            }else {
                //跨上未有占用
                frameRemainVO.setFrameBearWeight(new BigDecimal(0));
                frameRemainVO.setFrameWidth(new BigDecimal(0));
            }

            return frameRemainVO;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }

    }


    /**
     * 获取已占用库位和跨的map
     *
     * @param binIns
     * @return
     */
    public Map<String, String> getUsedBins(List<BinIn> binIns) {

        Map<String, String> usedBins = new HashMap<>();
        //实际bin

        if (CollectionUtils.isNotEmpty(binIns)) {
            Map<String, String> collect = binIns.stream().filter(a->a.getActualBinCode()!=null&&a.getActualFrameCode()!=null).collect(Collectors.toMap(BinIn::getActualBinCode,
                    BinIn::getActualFrameCode));
            usedBins.putAll(collect);
        }
        //推荐bin
        List<BinIn> recommendBins =
                binIns.stream().filter(r -> StringUtils.isEmpty(r.getActualBinCode()) && StringUtils.isNotEmpty(r.getRecommendBinCode()))
                        .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(recommendBins)) {
            Map<String, String> collect =
                    recommendBins.stream().collect(Collectors.toMap(BinIn::getRecommendBinCode,
                            BinIn::getRecommendFrameCode));
            usedBins.putAll(collect);
        }
        return usedBins;
    }
}
