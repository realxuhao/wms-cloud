package com.bosch.masterdata.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.masterdata.api.domain.Nmd;
import com.bosch.masterdata.api.domain.dto.NmdDTO;
import com.bosch.masterdata.api.domain.vo.NmdVO;

import java.util.List;

/**
* @author GUZ1CGD4
* @description 针对表【md_nmd(nmd主数据)】的数据库操作Mapper
* @createDate 2023-02-07 09:55:28
* @Entity com.bosch.masterdata.domain.Nmd
*/
public interface NmdMapper extends BaseMapper<Nmd> {

    /**
     * 查询nmdlist
     * @param nmdDTO
     * @return
     */
    public List<NmdVO> selectList(NmdDTO nmdDTO);

}




