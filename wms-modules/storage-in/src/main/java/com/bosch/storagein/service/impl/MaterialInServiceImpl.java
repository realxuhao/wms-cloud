package com.bosch.storagein.service.impl;

import com.bosch.masterdata.api.RemoteMaterialService;
import com.bosch.masterdata.api.domain.vo.MaterialVO;
import com.bosch.storagein.constants.Constants;
import com.bosch.storagein.domain.dto.MaterialInCheckDTO;
import com.bosch.storagein.domain.dto.MaterialInDTO;
import com.bosch.storagein.domain.dto.MaterialReceiveDTO;
import com.bosch.storagein.domain.vo.MaterialCheckResultVO;
import com.bosch.storagein.domain.vo.MaterialInCheckVO;
import com.bosch.storagein.domain.vo.MaterialInVO;
import com.bosch.storagein.domain.vo.MaterialReceiveVO;
import com.bosch.storagein.enumeration.CheckTypeEnum;
import com.bosch.storagein.enumeration.MaterialStatusEnum;
import com.bosch.storagein.mapper.MaterialInMapper;
import com.bosch.storagein.mapper.MaterialRecevieMapper;
import com.bosch.storagein.service.IMaterialInService;
import com.bosch.storagein.utils.BeanConverUtil;
import com.bosch.storagein.utils.MesBarCodeUtil;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 入库service
 */
@Service
public class MaterialInServiceImpl implements IMaterialInService {

    @Autowired
    private RemoteMaterialService remoteMaterialService;

    @Autowired
    private MaterialRecevieMapper materialRecevieMapper;

    @Autowired
    private MaterialInMapper materialInMapper;

    @Override
    public MaterialInCheckVO getMaterialSampleInfo(String mesBarCode) {
        MaterialVO materialVO = getMaterialVOByMesBarCode(mesBarCode);
        MaterialInCheckVO materialInCheckVO = buildMaterialCheckVO(materialVO, mesBarCode);
        materialRecevieMapper.updateStatusBySscc(materialInCheckVO.getSsccNumber(), MaterialStatusEnum.WAIT_IN.getCode());
        return materialInCheckVO;
    }

    @Override
    public MaterialCheckResultVO check(MaterialInCheckDTO materialInCheckDTO) {

        MaterialCheckResultVO materialCheckResultVO = dealCheck(materialInCheckDTO);

        //校验通过后，更新收货表状态+分配虚拟货位
        if (materialCheckResultVO.getCheckFlag()) {

            materialRecevieMapper.updateStatusBySscc(MesBarCodeUtil.getSSCC(materialInCheckDTO.getMesBarCode()),
                    MaterialStatusEnum.IN.getCode());

            materialInMapper.updateVirtualBinCode(materialCheckResultVO.getSsccNumber(), Constants.VIRTUAL_BIN_CODE);
        }


        return materialCheckResultVO;
    }

    @Override
    public MaterialInVO selectById(Long id) {
        MaterialInVO materialInVO = materialInMapper.selectById(id);

        return materialInVO;
    }

    @Override
    public List<MaterialInVO> selectBySsccNumber(String mesBarCode) {
        String sscc = MesBarCodeUtil.getSSCC(mesBarCode);
        return materialInMapper.selectBySsccNumber(sscc);
    }

    @Override
    public List<MaterialInVO> getByUserName(String username) {
        return materialInMapper.getByUserName(username);
    }

    @Override
    public List<MaterialInVO> selectMaterialInList(MaterialInDTO materialInDTO) {
        return materialInMapper.selectMaterialInList(materialInDTO);
    }

    private MaterialCheckResultVO dealCheck(MaterialInCheckDTO materialInCheckDTO) {
        Integer actualQuantity = materialInCheckDTO.getActualQuantity();
        Double actualResult = materialInCheckDTO.getActualResult();
        String mesBarCode = materialInCheckDTO.getMesBarCode();
        MaterialVO materialVO = getMaterialVOByMesBarCode(mesBarCode);
        MaterialInCheckVO materialInCheckVO = buildMaterialCheckVO(materialVO, mesBarCode);

        MaterialCheckResultVO checkResultVO = BeanConverUtil.conver(materialInCheckVO, MaterialCheckResultVO.class);
        checkResultVO.setOperateUser(SecurityUtils.getUsername());
        checkResultVO.setOperateTime(new Date());
        //如果是免检或者该批次已经检验过，直接入库。
        if (CheckTypeEnum.FREE.getCode().equals(materialInCheckVO.getCheckType()) ||
                CheckTypeEnum.CHECKED.getCode().equals(materialInCheckVO.getCheckType())) {
            MaterialInDTO materialInDTO = buildMaterialInDTO(mesBarCode, materialInCheckVO);
            materialInMapper.insertMaterialIn(materialInDTO);
            checkResultVO.setCheckFlag(true);
            return checkResultVO;
        }

        //称重 或者 数数
        if ((CheckTypeEnum.WEIGHT.getCode().equals(materialInCheckVO.getCheckType()) && checkWeight(materialInCheckVO, mesBarCode, actualQuantity, actualResult, checkResultVO))
                || (CheckTypeEnum.COUNT.getCode().equals(materialInCheckVO.getCheckType()) && checkCount(materialInCheckVO, actualQuantity, actualResult, checkResultVO))) {
            MaterialInDTO materialInDTO = buildMaterialInDTO(mesBarCode, materialInCheckVO);
            materialInDTO.setActualQuantity(actualQuantity);
            materialInDTO.setActualResult(actualResult);
            materialInDTO.setAverageResult(checkResultVO.getAverageResult());
            materialInMapper.insertMaterialIn(materialInDTO);

            checkResultVO.setActualResult(actualResult).setActualQuantity(actualQuantity);
            checkResultVO.setCheckFlag(true);
            return checkResultVO;
        }
        checkResultVO.setCheckFlag(false);
        return checkResultVO;
    }


    private MaterialInDTO buildMaterialInDTO(String mesBarCode, MaterialInCheckVO materialInCheckVO) {
        MaterialInDTO materialInDTO = new MaterialInDTO();
        materialInDTO.setSsccNumber(MesBarCodeUtil.getSSCC(mesBarCode));
        materialInDTO.setBatchNb(MesBarCodeUtil.getBatchNb(mesBarCode));
        materialInDTO.setMaterialNb(MesBarCodeUtil.getMaterialNb(mesBarCode));
        materialInDTO.setCheckQuantity(materialInCheckVO.getCheckQuantity());
        materialInDTO.setCheckType(materialInCheckVO.getCheckType());
        materialInDTO.setOperateUser(SecurityUtils.getUsername());
        materialInDTO.setOperateTime(new Date());
        materialInDTO.setMinStandard(materialInCheckVO.getMinStandard());
        materialInDTO.setMaxStandard(materialInCheckVO.getMaxStandard());
        return materialInDTO;
    }

    /**
     * 校验称重
     *
     * @param materialInCheckVO
     * @param actualQuantity
     * @param actualResult
     * @return
     */
    private Boolean checkWeight(MaterialInCheckVO materialInCheckVO, String mesBarCode, Integer actualQuantity,
                                Double actualResult, MaterialCheckResultVO checkResultVO) {
        if (actualQuantity < materialInCheckVO.getCheckQuantity()) {
            return false;
        }

        //计算平均值
        //TODO 补充计算说明
        MaterialVO materialVO = getMaterialVOByMesBarCode(mesBarCode);
        double caculateResult = (actualResult - materialVO.getPackageWeight().doubleValue()) / actualQuantity - (materialVO.getPackageWeight().doubleValue() - materialVO.getMinPackageNetWeight().doubleValue());
        double res = caculateResult / materialVO.getTransferWeightRatio().doubleValue();

        checkResultVO.setAverageResult(res);

        if (res < materialVO.getLessDeviationRatio().doubleValue() || res > materialVO.getMoreDeviationRatio().doubleValue()) {
            return false;
        }
        return true;

    }

    /**
     * 校验数数
     *
     * @param materialInCheckVO
     * @param actualQuantity
     * @param actualResult
     * @return
     */
    private Boolean checkCount(MaterialInCheckVO materialInCheckVO, Integer actualQuantity,
                               Double actualResult, MaterialCheckResultVO checkResultVO) {
        if (actualQuantity < materialInCheckVO.getCheckQuantity()) {
            return false;
        }
        double res = Math.ceil(actualResult / actualQuantity);
        checkResultVO.setAverageResult(res);
        //TODO 补充计算说明
        if (res < materialInCheckVO.getMinStandard() ||
                res > materialInCheckVO.getMaxStandard()) {
            return false;
        }
        return true;
    }

    /**
     * 根据mesBarCode获取物料信息
     *
     * @param mesBarCode
     * @return
     */
    private MaterialVO getMaterialVOByMesBarCode(String mesBarCode) {
        R<MaterialVO> materialVORes = remoteMaterialService.getInfoByMaterialCode(MesBarCodeUtil.getMaterialNb(mesBarCode));
        return materialVORes.getData();
    }

    /**
     * 构建MaterialCheckVO
     *
     * @param materialVO
     * @param mesBarCode
     * @return
     */
    private MaterialInCheckVO buildMaterialCheckVO(MaterialVO materialVO, String mesBarCode) {
        MaterialInCheckVO materialInCheckVO = new MaterialInCheckVO();
        materialInCheckVO.setSsccNumber(MesBarCodeUtil.getSSCC(mesBarCode));
        materialInCheckVO.setBatchNb(MesBarCodeUtil.getBatchNb(mesBarCode));
        materialInCheckVO.setMaterialNb(MesBarCodeUtil.getMaterialNb(mesBarCode));

        dealCheckType(materialInCheckVO, materialVO.getErrorProofingMethod());
        //免检或者该批次已经检验过，直接返回
        if (CheckTypeEnum.CHECKED.getCode().equals(materialInCheckVO.getCheckType()) || CheckTypeEnum.FREE.getCode().equals(materialInCheckVO.getCheckType())) {
            return materialInCheckVO;
        }
        materialInCheckVO.setMinStandard(materialVO.getLessDeviationRatio().doubleValue());
        materialInCheckVO.setMaxStandard(materialVO.getMoreDeviationRatio().doubleValue());
        materialInCheckVO.setUnit(materialVO.getUnit());

        //计算抽样数量
        dealCheckQuantity(materialInCheckVO, materialVO.getMinPackageNumber());

        MaterialReceiveVO materialReceiveVO = materialRecevieMapper.selectMaterialReceiveVOBySncc(materialInCheckVO.getSsccNumber());
        materialInCheckVO.setFromPurchaseOrder(materialReceiveVO.getFromPurchaseOrder());
        materialInCheckVO.setPoNumberItem(materialReceiveVO.getPoNumberItem());

        return materialInCheckVO;

    }

    private void dealCheckQuantity(MaterialInCheckVO materialInCheckVO, Long minPackageNumber) {
        MaterialReceiveDTO searchDTO = new MaterialReceiveDTO();
        searchDTO.setMaterialNb(materialInCheckVO.getMaterialNb());
        searchDTO.setBatchNumber(materialInCheckVO.getBatchNb());
        List<MaterialReceiveVO> list = materialRecevieMapper.selectMaterialReceiveVOList(searchDTO);

        //获取该物料下的该批次的总数量
        int total = list.stream().mapToInt(MaterialReceiveVO::getQuantity).sum();
        //计算最小包装数量
        Integer totalPackage = (int) Math.ceil(Double.valueOf(total / minPackageNumber));

        materialInCheckVO.setCheckQuantity(getCheckQuantity(totalPackage));

    }

    /**
     * 处理检验类型
     *
     * @param materialInCheckVO
     * @param errorProofingMethod
     */
    private void dealCheckType(MaterialInCheckVO materialInCheckVO, String errorProofingMethod) {
        MaterialReceiveDTO searchDTO = new MaterialReceiveDTO();
        searchDTO.setMaterialNb(materialInCheckVO.getMaterialNb());
        searchDTO.setBatchNumber(materialInCheckVO.getBatchNb());
        List<MaterialReceiveVO> list = materialRecevieMapper.selectMaterialReceiveVOList(searchDTO);

        List<Integer> status = list.stream().map(MaterialReceiveVO::getStatus).collect(Collectors.toList());

        if (status.contains(MaterialStatusEnum.IN.getCode())) {//该批次已经检验过
            materialInCheckVO.setCheckType(CheckTypeEnum.CHECKED.getCode());
            materialInCheckVO.setCheckTypeDesc(CheckTypeEnum.CHECKED.getDesc());
        } else if (CheckTypeEnum.COUNT.getDesc().equals(errorProofingMethod)) {//数数
            materialInCheckVO.setCheckType(CheckTypeEnum.COUNT.getCode());
            materialInCheckVO.setCheckTypeDesc(CheckTypeEnum.COUNT.getDesc());
        } else if (CheckTypeEnum.WEIGHT.getDesc().equals(errorProofingMethod)) {//称重
            materialInCheckVO.setCheckType(CheckTypeEnum.WEIGHT.getCode());
            materialInCheckVO.setCheckTypeDesc(CheckTypeEnum.WEIGHT.getDesc());
        } else if (CheckTypeEnum.FREE.getDesc().equals(errorProofingMethod)) {//免检
            materialInCheckVO.setCheckType(CheckTypeEnum.FREE.getCode());
            materialInCheckVO.setCheckTypeDesc(CheckTypeEnum.FREE.getDesc());
        }
    }

    /**
     * 抽样规则。TODO 需要放到数据库维护
     *
     * @param totalPackage
     * @return
     */
    private Integer getCheckQuantity(Integer totalPackage) {
        if (totalPackage >= 1 && totalPackage <= 10) {
            return totalPackage;
        } else if (totalPackage >= 1 && totalPackage <= 50) {
            return 10;
        } else if (totalPackage >= 51 && totalPackage <= 99) {
            return 13;
        } else if (totalPackage >= 100 && totalPackage <= 500) {
            return 50;
        } else if (totalPackage >= 501 && totalPackage <= 3200) {
            return 80;
        } else if (totalPackage > 3200) {
            return 125;
        }
        return 0;
    }


}
