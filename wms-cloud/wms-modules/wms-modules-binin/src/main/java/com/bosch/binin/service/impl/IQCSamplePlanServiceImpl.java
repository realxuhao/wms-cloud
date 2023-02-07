package com.bosch.binin.service.impl;

import com.bosch.binin.api.domain.dto.IQCSamplePlanQueryDTO;
import com.bosch.binin.api.domain.vo.IQCSamplePlanVO;
import com.bosch.binin.mapper.IQCSamplePlanMapper;
import com.bosch.binin.service.IIQCSamplePlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-02-07 16:15
 **/
@Service
public class IQCSamplePlanServiceImpl implements IIQCSamplePlanService {

    @Autowired
    private IQCSamplePlanMapper samplePlanMapper;

    @Override
    public List<IQCSamplePlanVO> getSamplePlan(IQCSamplePlanQueryDTO dto) {
        return samplePlanMapper.getSamplePlan(dto);
    }
}
