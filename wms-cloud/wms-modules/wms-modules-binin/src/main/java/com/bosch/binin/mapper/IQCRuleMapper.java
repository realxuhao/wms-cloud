package com.bosch.binin.mapper;

import com.bosch.binin.api.domain.NMDIQCRule;
import com.bosch.binin.api.domain.dto.NMDIQCRuleDTO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-02-07 10:21
 **/
@Mapper
public interface IQCRuleMapper {

    NMDIQCRule getNMDIQCRule(NMDIQCRuleDTO dto);

    NMDIQCRule getNMDIQCNumber(NMDIQCRuleDTO dto);
}
