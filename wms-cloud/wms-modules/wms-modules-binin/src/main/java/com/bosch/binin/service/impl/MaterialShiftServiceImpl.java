package com.bosch.binin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.binin.api.domain.MaterialKanban;
import com.bosch.binin.api.domain.MaterialShift;
import com.bosch.binin.mapper.MaterialKanbanMapper;
import com.bosch.binin.mapper.MaterialShiftMapper;
import com.bosch.binin.service.IMaterialShiftService;
import org.springframework.stereotype.Service;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-11-16 20:48
 **/
@Service
public class MaterialShiftServiceImpl  extends ServiceImpl<MaterialShiftMapper, MaterialShift> implements IMaterialShiftService {
}
