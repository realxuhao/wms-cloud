package com.bosch.binin.mapper;

import com.bosch.binin.api.domain.dto.IQCSamplePlanQueryDTO;
import com.bosch.binin.api.domain.vo.IQCSamplePlanVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-02-07 15:44
 **/
@Mapper
public interface IQCSamplePlanMapper {

    List<IQCSamplePlanVO> getSamplePlan(IQCSamplePlanQueryDTO dto);
}
