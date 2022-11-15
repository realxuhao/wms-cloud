package com.bosch.binin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.binin.api.domain.BinIn;
import com.bosch.binin.api.domain.MaterialKanban;
import com.bosch.binin.mapper.BinInMapper;
import com.bosch.binin.mapper.MaterialKanbanMapper;
import com.bosch.binin.service.IBinInService;
import com.bosch.binin.service.IMaterialKanbanService;
import org.springframework.stereotype.Service;

@Service
public class MaterialKanbanServiceImpl  extends ServiceImpl<MaterialKanbanMapper, MaterialKanban> implements IMaterialKanbanService {
}
