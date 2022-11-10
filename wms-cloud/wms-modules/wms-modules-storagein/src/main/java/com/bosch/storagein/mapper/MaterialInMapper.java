package com.bosch.storagein.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.masterdata.api.domain.Bin;
import com.bosch.storagein.api.domain.MaterialReceive;
import com.bosch.storagein.api.domain.dto.MaterialInDTO;
import com.bosch.storagein.api.domain.dto.MaterialQueryDTO;
import com.bosch.storagein.api.domain.vo.MaterialInVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: UWH4SZH
 * @since: 10/8/2022 14:36
 * @description:
 */
@Mapper
@Repository("materialInMapper")
public interface MaterialInMapper extends BaseMapper<MaterialInDTO>{

    /**
     * 新增入库
     *
     * @param materialInDTO 区域
     * @return 结果
     */
    public int insertMaterialIn(MaterialInDTO materialInDTO);

    MaterialInVO selectById(Long id);

    int updateVirtualBinCode(@Param("ssccNumber") String ssccNumber, @Param("virtualBinCode") String virtualBinCode);

    MaterialInVO selectBySsccNumber(String ssccNumber);

    List<MaterialInVO> getByUserName(String username);

    List<MaterialInVO> selectMaterialInList(MaterialQueryDTO queryDTO);

    void batchInsert(@Param("list") List<MaterialInDTO> materialInDTOList);
}
