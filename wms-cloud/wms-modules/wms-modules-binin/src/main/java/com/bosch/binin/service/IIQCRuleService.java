package com.bosch.binin.service;

import com.bosch.binin.api.domain.NMDIQCRule;
import com.bosch.binin.api.domain.dto.NMDIQCRuleDTO;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-02-07 10:16
 **/
public interface IIQCRuleService {

    NMDIQCRule getNMDIQCRule(NMDIQCRuleDTO dto);

}
