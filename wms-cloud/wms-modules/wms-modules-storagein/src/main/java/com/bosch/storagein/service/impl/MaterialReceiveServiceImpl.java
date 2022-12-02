package com.bosch.storagein.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.storagein.api.domain.MaterialReceive;
import com.bosch.storagein.api.domain.dto.*;
import com.bosch.storagein.api.domain.vo.*;
import com.bosch.storagein.api.enumeration.MaterialStatusEnum;
import com.bosch.storagein.mapper.MaterialRecevieMapper;
import com.bosch.storagein.service.IMaterialReceiveService;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.MesBarCodeUtil;
import com.ruoyi.common.core.utils.bean.BeanConverUtil;
import com.ruoyi.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MaterialReceiveServiceImpl extends ServiceImpl<MaterialRecevieMapper, MaterialReceive> implements IMaterialReceiveService {

    @Autowired
    private MaterialRecevieMapper materialRecevieMapper;


    @Override

    public List<MaterialReceiveVO> selectMaterialReceiveList(MaterialReceiveDTO materialReceiveDTO) {
        return materialRecevieMapper.selectMaterialReceiveVOList(materialReceiveDTO);
    }

    @Override
    public Integer deleteMaterialReceiveById(Long id) {
        MaterialReceive materialReceive = materialRecevieMapper.selectById(id);
        if (materialReceive.getStatus().equals(MaterialStatusEnum.IN.getCode())) {
            throw new ServiceException("已入库状态的收货信息不可删除！");
        }
        materialReceive.setUpdateUser(SecurityUtils.getUsername());
        materialReceive.setUpdateTime(new Date());
        materialReceive.setDeleteFlag(DeleteFlagStatus.TRUE.getCode());

        return materialRecevieMapper.updateById(materialReceive);
    }

    @Override
    public Integer deleteMaterialReceiveByIds(Long[] ids) {
        List<Long> longList = Arrays.asList(ids);
        List<MaterialReceive> materialReceives = materialRecevieMapper.selectBatchIds(longList);
        List<MaterialReceive> materialReceiveList = materialReceives.stream().filter(item -> item.getStatus().equals(MaterialStatusEnum.IN.getCode())).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(materialReceiveList)) {
            throw new ServiceException("已入库状态的收货信息不可删除！");
        }
        return materialRecevieMapper.deleteMaterialReceiveVOByIds(ids);
    }

    @Override
    public List<MaterialReceiveVO> selectSameBatchMaterial(String mesBarCode) {
        MaterialReceiveDTO materialReceiveDTO = new MaterialReceiveDTO();
        materialReceiveDTO.setMaterialNb(MesBarCodeUtil.getMaterialNb(mesBarCode));
        materialReceiveDTO.setBatchNb(MesBarCodeUtil.getBatchNb(mesBarCode));

        return materialRecevieMapper.selectMaterialReceiveVOList(materialReceiveDTO);
    }

    @Override
    public MaterialReceiveVO selectById(Long id) {
        return materialRecevieMapper.selectMaterialReceiveVOById(id);
    }

    @Override
    public Integer updateEditFlag(List<Long> ids, Integer editFlag) {
        return materialRecevieMapper.updateEditFlag(ids, editFlag);
    }


    public boolean validList(List<String> codes) {

        LambdaQueryWrapper<MaterialReceive> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(MaterialReceive::getSsccNumber, codes);
        Integer res = materialRecevieMapper.selectCount(lambdaQueryWrapper);
        return res > 0;
    }

    @Override
    public boolean updateBatchReceive(MaterialReceive materialReceive) {
        return materialRecevieMapper.updateBatch(materialReceive) > 0;
    }

    @Override
    public boolean validReceive(List<String> codes) {

        LambdaQueryWrapper<MaterialReceive> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MaterialReceive::getStatus, MaterialStatusEnum.IN.getCode());
        lambdaQueryWrapper.in(MaterialReceive::getSsccNumber, codes);
        Integer res = materialRecevieMapper.selectCount(lambdaQueryWrapper);
        return res > 0;
    }

    @Override
    public List<MaterialReceive> selectByMesBarCode(String mesbarCode) {
        LambdaQueryWrapper<MaterialReceive> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MaterialReceive::getSsccNumber, MesBarCodeUtil.getSSCC(mesbarCode));
        lambdaQueryWrapper.eq(MaterialReceive::getMaterialNb, MesBarCodeUtil.getMaterialNb(mesbarCode));
        lambdaQueryWrapper.eq(MaterialReceive::getBatchNb, MesBarCodeUtil.getBatchNb(mesbarCode));
        lambdaQueryWrapper.eq(MaterialReceive::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        return materialRecevieMapper.selectList(lambdaQueryWrapper);
    }
}
