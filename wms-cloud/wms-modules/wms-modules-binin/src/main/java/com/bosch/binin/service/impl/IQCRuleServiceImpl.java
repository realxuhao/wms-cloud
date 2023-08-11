package com.bosch.binin.service.impl;

import com.bosch.binin.api.domain.NMDIQCRule;
import com.bosch.binin.api.domain.dto.NMDIQCRuleDTO;
import com.bosch.binin.mapper.IQCRuleMapper;
import com.bosch.binin.service.IIQCRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-02-07 10:20
 **/
@Service
public class IQCRuleServiceImpl implements IIQCRuleService {

    @Autowired
    private IQCRuleMapper ruleMapper;

    @Override
    public NMDIQCRule getNMDIQCRule(NMDIQCRuleDTO dto) {
        return ruleMapper.getNMDIQCRule(dto);
    }

    @Override
    public NMDIQCRule getNMDIQCNumber(NMDIQCRuleDTO dto) {
        return ruleMapper.getNMDIQCNumber(dto);
    }
}
