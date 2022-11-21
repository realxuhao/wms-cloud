package com.bosch.binin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.binin.api.domain.MaterialShift;
import com.bosch.binin.api.domain.dto.AddShiftTaskDTO;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-11-16 20:48
 **/
public interface IMaterialShiftService extends IService<MaterialShift> {
    void addShiftRequirement(AddShiftTaskDTO dto);
}
