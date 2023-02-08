package com.bosch.masterdata.service;

import com.bosch.masterdata.api.domain.Nmd;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.masterdata.api.domain.dto.NmdDTO;
import com.bosch.masterdata.api.domain.vo.NmdVO;

import java.util.List;

/**
* @author GUZ1CGD4
* @description 针对表【md_nmd(nmd主数据)】的数据库操作Service
* @createDate 2023-02-07 09:55:28
*/
public interface INmdService extends IService<Nmd> {

    List<NmdVO> selectList(NmdDTO nmdDTO);
}
