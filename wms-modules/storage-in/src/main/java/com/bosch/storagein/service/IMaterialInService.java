package com.bosch.storagein.service;

import com.bosch.storagein.domain.dto.MaterialInCheckDTO;
import com.bosch.storagein.domain.vo.MaterialInCheckVO;

public interface IMaterialInService {

    /**
     * 查询抽样信息
     *
     * @param mesBarCode
     * @return
     */
    public MaterialInCheckVO getMaterialSampleInfo(String mesBarCode);


    public Boolean check(MaterialInCheckDTO materialInCheckDTO);


}
