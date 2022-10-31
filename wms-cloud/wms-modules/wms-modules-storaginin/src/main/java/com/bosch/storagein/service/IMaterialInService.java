package com.bosch.storagein.service;



import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.storagein.api.domain.MaterialReceive;
import com.bosch.storagein.api.domain.dto.MaterialInCheckDTO;
import com.bosch.storagein.api.domain.dto.MaterialInDTO;
import com.bosch.storagein.api.domain.vo.MaterialCheckResultVO;
import com.bosch.storagein.api.domain.vo.MaterialInCheckVO;
import com.bosch.storagein.api.domain.vo.MaterialInVO;
import com.ruoyi.common.core.domain.R;

import java.util.List;

public interface IMaterialInService extends IService<MaterialInDTO> {

    /**
     * 查询抽样信息
     *
     * @param mesBarCode
     * @return
     */
    public MaterialInCheckVO getMaterialCheckInfo(String mesBarCode);


    public MaterialCheckResultVO check(MaterialInCheckDTO materialInCheckDTO);


    MaterialInVO selectById(Long id);

    MaterialInVO selectByMesBarCode(String mesBarCode);

    List<MaterialInVO> getByUserName(String username);

    List<MaterialInVO> selectMaterialInList(MaterialInDTO materialInDTO);

    boolean checkSampleQuantity(MaterialInCheckDTO materialInCheckDTO);
}
