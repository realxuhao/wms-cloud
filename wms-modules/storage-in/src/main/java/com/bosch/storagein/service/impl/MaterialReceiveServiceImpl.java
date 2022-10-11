package com.bosch.storagein.service.impl;

import com.bosch.storagein.domain.dto.MaterialInDTO;
import com.bosch.storagein.domain.dto.MaterialReceiveDTO;
import com.bosch.storagein.domain.vo.MaterialReceiveVO;
import com.bosch.storagein.mapper.MaterialRecevieMapper;
import com.bosch.storagein.service.IMaterialReceiveService;
import com.bosch.storagein.utils.MesBarCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaterialReceiveServiceImpl implements IMaterialReceiveService {

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
}
