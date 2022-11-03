package com.bosch.binin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.binin.api.domain.Stock;
import com.bosch.binin.api.domain.dto.BinInDTO;
import com.bosch.binin.api.domain.dto.BinInTaskDTO;
import com.bosch.binin.api.enumeration.BinInStatusEnum;
import com.bosch.masterdata.api.RemoteMasterDataService;
import com.bosch.masterdata.api.domain.vo.BinVO;
import com.bosch.masterdata.api.domain.vo.MaterialBinVO;
import com.bosch.storagein.api.RemoteMaterialInService;
import com.bosch.binin.api.domain.BinIn;
import com.bosch.binin.api.domain.dto.BinInQueryDTO;
import com.bosch.binin.api.domain.vo.BinInVO;
import com.bosch.binin.mapper.BinInMapper;
import com.bosch.binin.mapper.StockMapper;
import com.bosch.binin.service.IBinInService;
import com.bosch.masterdata.api.domain.Pallet;
import com.bosch.masterdata.api.RemotePalletService;
import com.bosch.storagein.api.domain.vo.MaterialInVO;
import com.ruoyi.common.core.enums.MoveTypeEnums;
import com.ruoyi.common.core.utils.MesBarCodeUtil;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    private StockMapper stockMapper;


    @Autowired
    private RemotePalletService remotePalletService;

    @Autowired
    private RemoteMaterialInService remoteMaterialInService;

    @Autowired
    private RemoteMasterDataService remoteMasterDataService;

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
    public BinInVO getByMesBarCode(String mesBarCode) {
        String sscc = MesBarCodeUtil.getSSCC(mesBarCode);
        BinInVO binInVO = binInMapper.selectBySsccNumber(sscc);
        return binInVO;
    }

    @Override
    public List<BinInVO> currentUserData(BinInQueryDTO queryDTO) {
        return binInMapper.currentUserData(queryDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BinInVO performBinIn(BinInDTO binInDTO) {
        String mesBarCode = binInDTO.getMesBarCode();
        String sscc = MesBarCodeUtil.getSSCC(mesBarCode);
        String materialNb = MesBarCodeUtil.getMaterialNb(mesBarCode);

        LambdaQueryWrapper<BinIn> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BinIn::getSsccNumber, sscc);
        BinIn binIn = binInMapper.selectOne(lambdaQueryWrapper);


        if (binIn == null) {
            throw new ServiceException("该物料上架信息不存在");
        }

        BinVO actualBinVO = getBinVOByBinCode(binInDTO.getActualBinCode());

        if (!binInDTO.getActualBinCode().equals(binIn.getRecommendBinCode())) {
            //不在的时候，看actual bin code在不在分配规则内
            R<List<MaterialBinVO>> materialBinVOResullt = remoteMasterDataService.getListByMaterial(materialNb);
            if (StringUtils.isNull(materialBinVOResullt) || StringUtils.isNull(materialBinVOResullt.getData())) {
                throw new ServiceException("该物料：" + materialNb + " 暂无分配规则");
            }

            if (R.FAIL == materialBinVOResullt.getCode()) {
                throw new ServiceException(materialBinVOResullt.getMsg());
            }

            List<MaterialBinVO> materialBinVOS = materialBinVOResullt.getData();
            List<String> frameCodeList = materialBinVOS.stream().map(MaterialBinVO::getFrameTypeCode).collect(Collectors.toList());
            if (StringUtils.isEmpty(frameCodeList) || !frameCodeList.contains(actualBinVO.getFrameTypeCode())) {
                throw new ServiceException("该物料" + materialNb + " 不能分配到" + binInDTO.getActualBinCode());
            }
        }
        binIn.setActualFrameCode(actualBinVO.getFrameCode());
        binIn.setActualFrameId(actualBinVO.getFrameId());
        binIn.setUpdateBy(SecurityUtils.getUsername());
        binIn.setStatus(BinInStatusEnum.FINISH.value());
        binIn.setUpdateTime(new Date());
        saveOrUpdate(binIn);

        //插入库存
        Stock stock = new Stock();
        stock.setWareCode(actualBinVO.getWareCode());
        stock.setSsccNumber(binIn.getSsccNumber());
        stock.setWareCode(binIn.getWareCode());
        stock.setBinCode(binIn.getActualBinCode());
        stock.setMaterialNb(binIn.getMaterialNb());
        stock.setBatchNb(binIn.getBatchNb());
        stock.setExpireDate(binIn.getExpireDate());
        stock.setTotalStock(binIn.getQuantity());
        stock.setFreezeStock(0);
        stock.setAvailableStock(stock.getTotalStock() - stock.getFreezeStock());
        stock.setBinInId(binIn.getId());
        stock.setCreateBy(SecurityUtils.getUsername());
        stock.setCreateTime(new Date());
        stockMapper.insert(stock);


        //TODO 处理库存日志表


        return binInMapper.selectBySsccNumber(binIn.getSsccNumber());
    }

    private BinVO getBinVOByBinCode(String binCode) {
        R<BinVO> binInfoByCodeResult = remoteMasterDataService.getBinInfoByCode(binCode);
        if (StringUtils.isNull(binInfoByCodeResult) || StringUtils.isNull(binInfoByCodeResult.getData())) {
            throw new ServiceException("该库位：" + binCode + " 不存在");
        }

        if (R.FAIL == binInfoByCodeResult.getCode()) {
            throw new ServiceException(binInfoByCodeResult.getMsg());
        }
        return binInfoByCodeResult.getData();
    }

    private void validMaterialBinRule(BinVO binVO, String materialNb) {
        //不在的时候，看actual bin code在不在分配规则内
        R<List<MaterialBinVO>> materialBinVOResullt = remoteMasterDataService.getListByMaterial(materialNb);
        if (StringUtils.isNull(materialBinVOResullt) || StringUtils.isNull(materialBinVOResullt.getData())) {
            throw new ServiceException("该物料：" + materialNb + " 暂无分配规则");
        }

        if (R.FAIL == materialBinVOResullt.getCode()) {
            throw new ServiceException(materialBinVOResullt.getMsg());
        }

        List<MaterialBinVO> materialBinVOS = materialBinVOResullt.getData();
        List<String> frameCodeList = materialBinVOS.stream().map(MaterialBinVO::getFrameTypeCode).collect(Collectors.toList());
        if (StringUtils.isEmpty(frameCodeList) || !frameCodeList.contains(binVO.getFrameTypeCode())) {
            throw new ServiceException("该物料" + materialNb + " 不能分配到" + binVO.getCode());
        }
    }


    private MaterialInVO getMaterialInVO(String mesBarCode) {
        R<MaterialInVO> materialInVOResult = remoteMaterialInService.getByMesBarCode(mesBarCode);
        if (StringUtils.isNull(materialInVOResult) || StringUtils.isNull(materialInVOResult.getData())) {
            throw new ServiceException("该物料：" + mesBarCode + " 未入库");
        }

        if (R.FAIL == materialInVOResult.getCode()) {
            throw new ServiceException(materialInVOResult.getMsg());
        }

        return materialInVOResult.getData();
    }

    @Override
    public BinInVO generateInTask(BinInTaskDTO binInTaskDTO) {
        String mesBarCode = binInTaskDTO.getMesBarCode();
        String sscc = MesBarCodeUtil.getSSCC(mesBarCode);
        String materialNb = MesBarCodeUtil.getMaterialNb(mesBarCode);
        MaterialInVO materialInVO = getMaterialInVO(mesBarCode);

        //校验是否能放在这个库位
        BinVO binVO = getBinVOByBinCode(binInTaskDTO.getRecommendBinCode());
        validMaterialBinRule(binVO, materialNb);

        //校验承重

        BinIn binIn = new BinIn();
        binIn.setSsccNumber(sscc);
        binIn.setQuantity(materialInVO.getQuantity());
        binIn.setMaterialNb(materialNb);
        binIn.setBatchNb(materialInVO.getBatchNb());
        binIn.setExpireDate(MesBarCodeUtil.getExpireDate(mesBarCode));
        binIn.setPalletCode(binInTaskDTO.getPalletCode());
        binIn.setPalletType(binInTaskDTO.getPalletType());
        binIn.setRecommendBinCode(binInTaskDTO.getRecommendBinCode());
        binIn.setStatus(BinInStatusEnum.PROCESSING.value());
        binIn.setRecommendFrameId(binVO.getFrameId());
        binIn.setRecommendFrameCode(binVO.getFrameCode());
        binIn.setWareCode(SecurityUtils.getWareCode());
        binIn.setMoveType(MoveTypeEnums.BININ.getCode());

        binInMapper.insert(binIn);

        return getByMesBarCode(mesBarCode);
    }
}
