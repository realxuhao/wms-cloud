package com.bosch.masterdata.service;

import com.bosch.masterdata.api.domain.Nmd;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.masterdata.api.domain.dto.MaterialDTO;
import com.bosch.masterdata.api.domain.dto.NmdDTO;
import com.bosch.masterdata.api.domain.vo.NmdVO;

import java.util.List;

/**
* @author GUZ1CGD4
* @description 针对表【md_nmd(nmd主数据)】的数据库操作Service
* @createDate 2023-02-07 09:55:28
*/
public interface INmdService extends IService<Nmd> {

    /**
     * 查询nmdlist
     * @param nmdDTO
     * @return
     */
    List<NmdVO> selectList(NmdDTO nmdDTO);

    /**
     * 查询单个nmd 信息
     * @param id
     * @return
     */
    NmdVO selectNmdById(Long id);

    /**
     * 插入nmd
     * @param nmdDTO
     * @return
     */
    Integer insertNmd(NmdDTO nmdDTO);

    /**
     * 更新nmd
     * @param nmdDTO
     * @return
     */
    Integer updateNmd(NmdDTO nmdDTO);

    /**
     * 删除nmd
     * @param ids
     * @return
     */
    Integer deleteNmd(Long[] ids);

    /**
     * 查询信息是否重复
     *
     * @param nmdDTOS
     * @return
     */
    boolean validNmdList(List<NmdDTO> nmdDTOS);

    /**
     * 查询导入数据是否规范
     *
     * @param nmdDTOS
     * @return
     */
    boolean validData(List<NmdDTO> nmdDTOS);

}
