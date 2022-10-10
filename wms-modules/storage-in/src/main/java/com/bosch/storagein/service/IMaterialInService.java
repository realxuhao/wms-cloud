package com.bosch.storagein.service;

import com.bosch.storagein.domain.dto.MaterialInCheckDTO;
import com.bosch.storagein.domain.dto.MaterialInDTO;
import com.bosch.storagein.domain.vo.MaterialCheckResultVO;
import com.bosch.storagein.domain.vo.MaterialInCheckVO;
import com.bosch.storagein.domain.vo.MaterialInVO;

import java.util.List;

public interface IMaterialInService {

    /**
     * 查询抽样信息
     *
     * @param mesBarCode
     * @return
     */
    public MaterialInCheckVO getMaterialSampleInfo(String mesBarCode);


    public MaterialCheckResultVO check(MaterialInCheckDTO materialInCheckDTO);


    MaterialInVO selectById(Long id);

    List<MaterialInVO> selectBySsccNumber(String mesBarCode);

    List<MaterialInVO> getByUserName(String username);

    List<MaterialInVO> selectMaterialInList(MaterialInDTO materialInDTO);
}
