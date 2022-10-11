package com.bosch.storagein.mapper;

import com.bosch.storagein.api.domain.dto.MaterialInDTO;
import com.bosch.storagein.api.domain.vo.MaterialInVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    MaterialInVO selectById(Long id);

    int updateVirtualBinCode(@Param("ssccNumber") String ssccNumber, @Param("virtualBinCode") String virtualBinCode);

    List<MaterialInVO> selectBySsccNumber(String ssccNumber);

    List<MaterialInVO> getByUserName(String username);

    List<MaterialInVO> selectMaterialInList(MaterialInDTO materialInDTO);
}
