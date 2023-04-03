package com.bosch.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.bosch.binin.api.domain.BinIn;
import com.bosch.binin.api.domain.vo.FrameRemainVO;
import com.bosch.masterdata.api.RemoteMasterDataService;
import com.bosch.masterdata.api.RemoteMaterialService;
import com.bosch.masterdata.api.RemotePalletService;
import com.bosch.masterdata.api.domain.Bin;
import com.bosch.masterdata.api.domain.Pallet;
import com.bosch.masterdata.api.domain.vo.*;
import com.bosch.masterdata.api.enumeration.AreaTypeEnum;
import com.bosch.product.api.domain.ProductReceive;
import com.bosch.product.api.domain.ProductStock;
import com.bosch.product.api.domain.enumeration.ProductStockBinInEnum;
import com.bosch.product.service.IBinAssignmentService;
import com.bosch.product.service.IProductReceiveService;
import com.bosch.product.service.IProductStockService;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.ProductQRCodeUtil;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BinAssignmentServiceImpl implements IBinAssignmentService {

    @Autowired
    private RemotePalletService remotePalletService;


    @Autowired
    private RemoteMaterialService remoteMaterialService;

    @Autowired
    private RemoteMasterDataService remoteMasterDataService;


    @Autowired
    private IProductReceiveService productReceiveService;

    @Autowired
    private IProductStockService productStockService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String getBinAllocationVO(String qrCode) {


        String sscc = ProductQRCodeUtil.getSSCC(qrCode);
        String batchNb = ProductQRCodeUtil.getBatchNb(qrCode);

        //获取物料号
        ProductReceive productReceiveVO = productReceiveService.getProductReceiveVO(sscc);
        String materialNb = productReceiveVO.getMaterialNb();

        //获取同批次 同物料号 已占用跨 、库位
        LambdaQueryWrapper<ProductStock> stockQueryWrapper = new LambdaQueryWrapper<>();
        stockQueryWrapper.eq(ProductStock::getMaterialNb, materialNb);
        stockQueryWrapper.eq(ProductStock::getBatchNb, batchNb);
        stockQueryWrapper.eq(ProductStock::getBinInFlag, ProductStockBinInEnum.FINISH.code());
        stockQueryWrapper.eq(ProductStock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        List<ProductStock> productStockList = productStockService.list(stockQueryWrapper);

        //已占库位和跨的map
        Map<String, String> usedBins = getUsedBins(productStockList);

        //可用库位
        List<ProductFrameVO> productFrameVOList = new ArrayList<>();

        List<String> frameTypeCodes = new ArrayList<>();
        //根据物料号 登陆人仓库 获取物料跨关系
        R<List<ProductFrameVO>> productFrameListR = remoteMasterDataService.getProductFrame(materialNb, SecurityUtils.getWareCode());
        if (!productFrameListR.isSuccess()) {
            throw new ServiceException("获取成品相关跨失败");
        }
        if (CollectionUtils.isEmpty(productFrameListR.getData())) {
            throw new ServiceException("该成品未维护可用跨");
        }
        //跨类型根据优先级排序
        productFrameVOList = productFrameListR.getData().stream().
                sorted(Comparator.comparing(ProductFrameVO::getPriorityLevel, Comparator.naturalOrder())).
                collect(Collectors.toList());
        frameTypeCodes =
                productFrameVOList.stream().map(ProductFrameVO::getFrameTypeCode).collect(Collectors.toList());
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
            List<String> framesByType = framesVOByType.stream().filter(item->item.getWareCode().equals(SecurityUtils.getWareCode())).map(FrameVO::getCode).collect(Collectors.toList());
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
            //筛选出存储区为成品的库位
            binVOs = binVOs.stream().filter(item -> item.getWareCode().equals(SecurityUtils.getWareCode()) && AreaTypeEnum.PRO.getCode().equals(item.getAreaType())).collect(Collectors.toList());
            //跨下所有库位编码
            List<String> allBins = binVOs.stream().map(BinVO::getCode).collect(Collectors.toList());
            //判断是否有占用库位 usedBins allBins
            //有占用库位
            if (usedBins.size() > 0) {
                boolean disjoint = Collections.disjoint(usedBins.keySet(), allBins);
                //没有交集，占用库位不在该跨类型中
                if (disjoint) {
                    //查询下一个跨类型
                    continue;
                } else {
                    //todo 跨排序
                    List<String> usedFrames = usedBins.values().stream().distinct().collect(Collectors.toList());
                    List<String> usedSortFrames = sortList(usedFrames);
                    //根据排完序的库位，在库位list中查到当前占用跨，判断跨能否放入
                    String canUseCode = getCanUseCode(framesByType, usedSortFrames);
                    if (StringUtils.isNotEmpty(canUseCode)) {
                        return canUseCode;
                    } else {
                        continue;
                    }
                }
            } else {
                //未占用库位
                //根据排完序的库位，在库位list中查到当前占用跨，判断跨能否放入
                String canUseCode = getCanUseCode(framesByType, null);
                if (StringUtils.isNotEmpty(canUseCode)) {

                    return canUseCode;
                } else {
                    continue;
                }
            }
        }
        throw new ServiceException("未找到合适库位");
//        return binAllocationVO;
    }

    public List<String> sortList(List<String> frames) {
        List<String> changes = new ArrayList<>();
        List<String> result = new ArrayList<>();
        frames.forEach(r -> {
            String[] splitr = r.split("\\.");
            if (splitr.length < 3) {
                throw new ServiceException(r + "跨位编码有误，请去维护");
            }
            changes.add(splitr[0] + "." + splitr[2] + "." + splitr[1]);
        });
        Collections.sort(changes);
        changes.forEach(r -> {
            String[] splitr = r.split("\\.");
            result.add(splitr[0] + "." + splitr[2] + "." + splitr[1]);

        });
        return result;
    }

    /**
     * 获取可用库位
     *
     * @param framesUnSorted
     * @param framesUsed
     * @return
     */
    public String getCanUseCode(List<String> framesUnSorted, List<String> framesUsed) {

        if (CollectionUtils.isEmpty(framesUnSorted)) {
            return null;
        }
        List<String> framesByType = sortList(framesUnSorted);
        //未占用库位
        if (CollectionUtils.isEmpty(framesUsed)) {
            //list 要去重 排序
            for (int moveFlag = 0; moveFlag < framesByType.size(); moveFlag++) {
                //取下一个
                if (moveFlag < framesByType.size()) {
                    String binCode = validateBin(framesByType.get(moveFlag));
                    //返回库位
                    if (binCode != null) {
                        return binCode;
                    }
                }
            }
        } else {
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
                        String binCode = validateBin(framesByType.get(usedIndex - moveFlag));
                        //返回库位
                        if (binCode != null) {
                            return binCode;
                        }
                    }
                    //取下一个
                    if ((usedIndex + moveFlag) < framesByType.size()) {
                        String binCode = validateBin(framesByType.get(usedIndex + moveFlag));
                        //返回库位
                        if (binCode != null) {
                            return binCode;
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
     * @param
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public String validateBin(String frameCode) {
        try {
            FrameRemainVO frameRemainVO = new FrameRemainVO();
            String binCode = null;
            //获取bin关联跨，再获取跨承重，宽度
            R<FrameVO> frameInfoByCode = remoteMasterDataService.getFrameInfoByCode(frameCode);
            //R<BinVO> binResult = remoteMasterDataService.getBinInfoByCode(binCode);
            if (frameInfoByCode.isSuccess()) {
                FrameVO frameData = frameInfoByCode.getData();
                if (frameData != null && !frameData.getWareCode().equals(SecurityUtils.getWareCode())) {
                    return null;
                }
                if (frameData != null) {

                    //获取跨上剩余承重，宽度
                    binCode = getBins(frameData.getId(), frameCode);

                    return binCode;
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
    public String getBins(Long frameId, String frameCode) {
        try {
            List<String> usedBins = new ArrayList<>();
            //已上架 查询实际库位
            LambdaQueryWrapper<ProductStock> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ProductStock::getFrameCode, frameCode);
            queryWrapper.eq(ProductStock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
            queryWrapper.eq(ProductStock::getBinInFlag, ProductStockBinInEnum.FINISH.code());
            List<ProductStock> productStocks = productStockService.list(queryWrapper);

            if (CollectionUtils.isNotEmpty(productStocks)) {
                //获取已使用库位
                usedBins.addAll(productStocks.stream().map(ProductStock::getBinCode).collect(Collectors.toList()));
            }

            //先分配跨上最小库位
            R<List<Bin>> infoByFrameId = remoteMasterDataService.getInfoByFrameId(frameId);
            if (infoByFrameId.isSuccess()) {
                List<Bin> data = infoByFrameId.getData();
                if (CollectionUtils.isEmpty(data)) {
                    throw new ServiceException(frameCode + "跨上未获取到库位");
                }
                List<String> canUseBins = data.stream().map(Bin::getCode).collect(Collectors.toList());
                //自然排序
                Collections.sort(canUseBins);

                for (String r : canUseBins) {
                    if (!usedBins.contains(r)) {
                        return r;
                    }
                }

            } else {
                throw new ServiceException("根据frameid获取最小库位失败");
            }
            return null;

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
            frameRemainVO.setFrameBearWeight(new BigDecimal(0));
            frameRemainVO.setFrameWidth(new BigDecimal(0));
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
            } else {
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
     * @param productStockList
     * @return
     */
    public Map<String, String> getUsedBins(List<ProductStock> productStockList) {

        Map<String, String> usedBins = new HashMap<>();
        //实际bin

        if (CollectionUtils.isNotEmpty(productStockList)) {
            Map<String, String> collect = productStockList.stream().collect(Collectors.toMap(ProductStock::getBinCode,
                    ProductStock::getFrameCode));
            usedBins.putAll(collect);
        }

        return usedBins;
    }
}
