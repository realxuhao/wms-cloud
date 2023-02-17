package com.bosch.masterdata.service;

import com.bosch.masterdata.api.domain.Ecn;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.masterdata.api.domain.dto.EcnDTO;
import com.bosch.masterdata.api.domain.vo.EcnVO;
import com.bosch.masterdata.api.domain.vo.NmdVO;

import java.util.List;

/**
* @author GUZ1CGD4
* @description 针对表【md_ecn(ecn主数据)】的数据库操作Service
* @createDate 2023-02-17 11:20:47
*/
public interface IEcnService extends IService<Ecn> {
    /**
     * 查询EcnVOlist
     * @param ecnDTO
     * @return
     */
    List<EcnVO> selectList(EcnDTO ecnDTO);

    /**
     * 查询单个Ecn 信息
     * @param id
     * @return
     */
    EcnVO selectEcnById(Long id);

    /**
     * 插入EcnVO
     * @param ecnDTO
     * @return
     */
    Integer insertEcn(EcnDTO ecnDTO);

    /**
     * 更新EcnVO
     * @param ecnDTO
     * @return
     */
    Integer updateEcn(EcnDTO ecnDTO);

    /**
     * 删除EcnVO
     * @param ids
     * @return
     */
    Integer deleteEcn(Long[] ids);

    /**
     * 查询信息是否重复
     *
     * @param ecnDTOS
     * @return
     */
    boolean validEcnList(List<EcnDTO> ecnDTOS);

    /**
     * 查询导入数据是否规范
     *
     * @param ecnDTOS
     * @return
     */
    boolean validData(List<EcnDTO> ecnDTOS);
}
