package com.bosch.storagein.service;



import com.bosch.storagein.api.domain.dto.MaterialInCheckDTO;
import com.bosch.storagein.api.domain.dto.MaterialInDTO;
import com.bosch.storagein.api.domain.vo.MaterialCheckResultVO;
import com.bosch.storagein.api.domain.vo.MaterialInCheckVO;
import com.bosch.storagein.api.domain.vo.MaterialInVO;

import java.util.List;

public interface IMaterialInService {

    /**
     * 查询抽样信息
     *
     * @param mesBarCode
     * @return
     */
    public MaterialInCheckVO getMaterialCheckInfo(String mesBarCode);


    public MaterialCheckResultVO check(MaterialInCheckDTO materialInCheckDTO);


    MaterialInVO selectById(Long id);

    List<MaterialInVO> selectBySsccNumber(String mesBarCode);

    List<MaterialInVO> getByUserName(String username);

    List<MaterialInVO> selectMaterialInList(MaterialInDTO materialInDTO);
}
