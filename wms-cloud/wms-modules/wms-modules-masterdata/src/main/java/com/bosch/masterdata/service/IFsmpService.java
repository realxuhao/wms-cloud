package com.bosch.masterdata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.masterdata.api.domain.Fsmp;
import com.bosch.masterdata.api.domain.Fsmp;
import com.bosch.masterdata.api.domain.dto.FsmpDTO;
import com.bosch.masterdata.api.domain.vo.FsmpVO;
import com.bosch.masterdata.api.domain.vo.FsmpVO;

import java.util.List;


public interface IFsmpService extends IService<Fsmp> {
    /**
     * 查询VOlist
     * @param
     * @return
     */
    List<FsmpVO> selectList(FsmpDTO fsmpDTO);

    /**
     * 查询单个Fsmp 信息
     * @param id
     * @return
     */
    FsmpVO selectFsmpById(Long id);

    /**
     * 插入FsmpVO
     * @param
     * @return
     */
    Integer insertFsmp(FsmpDTO fsmpDTO);

    /**
     * 更新FsmpVO
     * @param
     * @return
     */
    Integer updateFsmp(FsmpDTO fsmpDTO);

    /**
     * 删除FsmpVO
     * @param ids
     * @return
     */
    Integer deleteFsmp(Long[] ids);

    /**
     * 查询信息是否重复
     *
     * @param
     * @return
     */
    boolean validFsmpList(List<FsmpDTO> fsmpDTOS);

    /**
     * 查询导入数据是否规范
     *
     * @param
     * @return
     */
    boolean validData(List<FsmpDTO> fsmpDTOS);

    Fsmp getByMaterialNb(String materialNb);
}
