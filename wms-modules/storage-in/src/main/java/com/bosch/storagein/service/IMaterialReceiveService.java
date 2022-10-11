package com.bosch.storagein.service;



import com.bosch.storagein.api.domain.dto.MaterialReceiveDTO;
import com.bosch.storagein.api.domain.vo.MaterialReceiveVO;

import java.util.List;

public interface IMaterialReceiveService {

    /**
     * 查询物料收货列表
     *
     * @param materialReceiveDTO 物料收货
     * @return 物料收货列表
     */
    public List<MaterialReceiveVO> selectMaterialReceiveList(MaterialReceiveDTO materialReceiveDTO);


    public Integer deleteMaterialReceiveById(Long id);


    public Integer deleteMaterialReceiveByIds(Long[] ids);

    public  List<MaterialReceiveVO> selectByMesBarCode(String mesBarCode);

    MaterialReceiveVO selectById(Long id);

    public Integer updateEditFlag(List<Long> id, Integer editFlag);
}
