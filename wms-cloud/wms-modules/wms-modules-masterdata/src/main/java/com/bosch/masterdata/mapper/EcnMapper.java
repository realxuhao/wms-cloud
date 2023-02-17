package com.bosch.masterdata.mapper;

import com.bosch.masterdata.api.domain.Ecn;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.masterdata.api.domain.dto.EcnDTO;
import com.bosch.masterdata.api.domain.dto.NmdDTO;
import com.bosch.masterdata.api.domain.vo.EcnVO;
import com.bosch.masterdata.api.domain.vo.NmdVO;

import java.util.List;

/**
* @author GUZ1CGD4
* @description 针对表【md_ecn(ecn主数据)】的数据库操作Mapper
* @createDate 2023-02-17 11:20:47
* @Entity com.bosch.masterdata.domain.MdEcn
*/
public interface EcnMapper extends BaseMapper<Ecn> {
    /**
     * 查询ecnlist
     * @param ecnDTO
     * @return
     */
    public List<EcnVO> selectList(EcnDTO ecnDTO);


    /**
     * 更新ecn
     * @param ecnDTO
     * @return
     */
    public Integer updateEcn(EcnDTO ecnDTO);

    /**
     * 删除ecn
     * @param ids
     * @return
     */
    public Integer deleteEcn(Long[] ids);

    /**
     * 验证是否重复
     * @param list
     * @return
     */
    public Integer validateRecord(List<EcnDTO> list);
}




