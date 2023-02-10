package com.bosch.masterdata.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.masterdata.api.domain.Nmd;
import com.bosch.masterdata.api.domain.dto.NmdDTO;
import com.bosch.masterdata.api.domain.vo.NmdVO;
import com.bosch.masterdata.service.INmdService;
import com.bosch.masterdata.mapper.NmdMapper;
import com.bosch.masterdata.utils.BeanConverUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author GUZ1CGD4
* @description 针对表【md_nmd(nmd主数据)】的数据库操作Service实现
* @createDate 2023-02-07 09:55:28
*/
@Service
public class NmdServiceImpl extends ServiceImpl<NmdMapper, Nmd>
    implements INmdService {

    @Autowired
    private NmdMapper nmdMapper;

    @Override
    public List<NmdVO> selectList(NmdDTO nmdDTO) {
        List<NmdVO> nmdVOS = nmdMapper.selectList(nmdDTO);
        return nmdVOS;
    }

    @Override
    public NmdVO selectNmdById(Long id) {
        Nmd nmd = nmdMapper.selectById(id);
        NmdVO nmdVO = BeanConverUtil.conver(nmd, NmdVO.class);

        return nmdVO;
    }

    @Override
    public Integer insertNmd(NmdDTO nmdDTO) {
        Nmd nmd = BeanConverUtil.conver(nmdDTO, Nmd.class);
        int insert = nmdMapper.insert(nmd);
        return insert;
    }

    @Override
    public Integer updateNmd(NmdDTO nmdDTO) {

        return nmdMapper.updateNmd(nmdDTO);
    }

    @Override
    public Integer deleteNmd(Long[] ids) {
        return nmdMapper.deleteNmd(ids);
    }

    @Override
    public boolean validNmdList(List<NmdDTO> nmdDTOS) {
        return nmdMapper.validateRecord(nmdDTOS)>0;
    }
}




