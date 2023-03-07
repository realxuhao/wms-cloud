package com.bosch.masterdata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.masterdata.api.domain.Ecn;
import com.bosch.masterdata.api.domain.Fsmp;
import com.bosch.masterdata.api.domain.dto.EcnDTO;
import com.bosch.masterdata.api.domain.dto.FsmpDTO;
import com.bosch.masterdata.api.domain.vo.EcnVO;
import com.bosch.masterdata.api.domain.vo.FsmpVO;

import java.util.List;


public interface FsmpMapper extends BaseMapper<Fsmp> {
    /**
     * 查询list
     * @param dto
     * @return
     */
    public List<FsmpVO> selectList(FsmpDTO dto);


    /**
     * 更新Fsmp
     * @param dto
     * @return
     */
    public Integer updateFsmp(FsmpDTO dto);

    /**
     * 删除ecn
     * @param ids
     * @return
     */
    public Integer deleteFsmp(Long[] ids);

    /**
     * 验证是否重复
     * @param list
     * @return
     */
    public Integer validateRecord(List<FsmpDTO> list);
}




