package com.bosch.storagein.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.storagein.api.domain.MaterialReceive;
import com.bosch.storagein.api.domain.dto.*;
import com.bosch.storagein.api.domain.vo.*;
import com.bosch.storagein.mapper.MaterialRecevieMapper;
import com.bosch.storagein.service.IMaterialReceiveService;
import com.ruoyi.common.core.utils.MesBarCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return materialRecevieMapper.deleteMaterialReceiveVOById(id);
    }

    @Override
    public Integer deleteMaterialReceiveByIds(Long[] ids) {
        return materialRecevieMapper.deleteMaterialReceiveVOByIds(ids);
    }

    @Override
    public List<MaterialReceiveVO> selectByMesBarCode(String mesBarCode) {
        MaterialReceiveDTO materialReceiveDTO = new MaterialReceiveDTO();
        materialReceiveDTO.setMaterialNb(MesBarCodeUtil.getMaterialNb(mesBarCode));
        materialReceiveDTO.setBatchNumber(MesBarCodeUtil.getBatchNb(mesBarCode));

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

        LambdaQueryWrapper<MaterialReceive> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(MaterialReceive::getSsccNumber,codes);
        Integer res = materialRecevieMapper.selectCount(lambdaQueryWrapper);
        return  res>0;
    }

    @Override
    public boolean updateBatch(MaterialReceive materialReceive) {
        return materialRecevieMapper.updateBatch(materialReceive)>0;
    }


}
