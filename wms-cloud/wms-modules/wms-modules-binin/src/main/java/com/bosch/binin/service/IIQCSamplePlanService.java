package com.bosch.binin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.binin.api.domain.IQCSamplePlan;
import com.bosch.binin.api.domain.ManualTransferOrder;
import com.bosch.binin.api.domain.dto.IQCSamplePlanDTO;
import com.bosch.binin.api.domain.dto.IQCSamplePlanQueryDTO;
import com.bosch.binin.api.domain.vo.IQCSamplePlanVO;

import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-02-07 16:14
 **/
public interface IIQCSamplePlanService extends IService<IQCSamplePlan> {
    List<IQCSamplePlanVO> getSamplePlan(IQCSamplePlanQueryDTO dto);

    void manualAdd(List<IQCSamplePlanDTO> dto);
}
