package com.bosch.binin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.binin.api.domain.MaterialCall;
import com.bosch.binin.api.domain.MaterialKanban;
import com.bosch.binin.api.domain.dto.MaterialKanbanDTO;
import com.bosch.binin.api.domain.vo.MaterialInfoVO;
import com.bosch.binin.api.domain.vo.MaterialKanbanVO;
import com.bosch.binin.api.domain.vo.StockVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository("materialKanbanMapper")
public interface MaterialKanbanMapper extends BaseMapper<MaterialKanban> {

    /**
     * 批量删除
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteByIds(Long[] ids);

    public List<MaterialKanbanVO> receivingMaterialList(MaterialKanbanDTO dto);

    public List<MaterialKanbanVO> receivedMaterialList(MaterialKanbanDTO dto);

    List<MaterialInfoVO> materialInfoList(String sscc, String wareCode);

    List<MaterialInfoVO> materialInfo(String sscc);

    List<MaterialInfoVO> materialInfoBySSCC(@Param("ssccList") List<String> ssccList);

    List<MaterialKanbanVO> getBinDownList(String wareCode);

    MaterialKanbanVO getKanbanInfoBySsccNb(String ssccNb);

    List<MaterialKanbanVO> waitingBinDownList(String wareCode);

    List<MaterialKanbanVO> getKanbanList(MaterialKanbanDTO dto);
}
