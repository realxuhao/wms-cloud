package com.bosch.storagein.mapper;

import com.bosch.masterdata.api.domain.Area;
import com.bosch.storagein.domain.dto.MaterialInDTO;

/**
 * @author: UWH4SZH
 * @since: 10/8/2022 14:36
 * @description:
 */
public interface MaterialInMapper {

    /**
     * 新增入库
     *
     * @param materialInDTO 区域
     * @return 结果
     */
    public int insertMaterialIn(MaterialInDTO materialInDTO);
}
