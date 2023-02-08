package com.bosch.binin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.binin.api.domain.IQCSamplePlan;
import com.bosch.binin.api.domain.ManualTransferOrder;
import com.bosch.binin.api.domain.dto.IQCSamplePlanQueryDTO;
import com.bosch.binin.api.domain.vo.IQCSamplePlanVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-02-07 15:44
 **/
@Mapper
public interface IQCSamplePlanMapper  extends BaseMapper<IQCSamplePlan> {

    List<IQCSamplePlanVO> getSamplePlan(IQCSamplePlanQueryDTO dto);
}
