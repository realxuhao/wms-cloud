package com.bosch.binin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.binin.api.domain.BinIn;
import com.bosch.binin.api.domain.MaterialKanban;
import com.bosch.binin.api.domain.dto.MaterialKanbanDTO;
import com.bosch.binin.api.domain.vo.MaterialKanbanVO;
import com.ruoyi.common.core.web.page.PageDomain;

public interface IMaterialKanbanService extends IService<MaterialKanban> {

    IPage<MaterialKanbanVO> pageList(MaterialKanbanDTO dto);
}
