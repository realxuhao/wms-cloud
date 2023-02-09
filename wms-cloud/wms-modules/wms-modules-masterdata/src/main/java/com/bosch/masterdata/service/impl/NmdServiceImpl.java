package com.bosch.masterdata.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.masterdata.api.domain.Nmd;
import com.bosch.masterdata.api.domain.dto.NmdDTO;
import com.bosch.masterdata.api.domain.vo.NmdVO;
import com.bosch.masterdata.service.INmdService;
import com.bosch.masterdata.mapper.NmdMapper;
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

        return null;
    }
}




