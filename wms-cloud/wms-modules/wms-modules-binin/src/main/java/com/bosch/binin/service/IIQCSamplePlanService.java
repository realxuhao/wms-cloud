package com.bosch.binin.service;

import com.bosch.binin.api.domain.dto.IQCSamplePlanQueryDTO;
import com.bosch.binin.api.domain.vo.IQCSamplePlanVO;

import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-02-07 16:14
 **/
public interface IIQCSamplePlanService {
    List<IQCSamplePlanVO> getSamplePlan(IQCSamplePlanQueryDTO dto);
}
