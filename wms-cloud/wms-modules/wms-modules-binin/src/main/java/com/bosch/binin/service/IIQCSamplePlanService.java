package com.bosch.binin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.binin.api.domain.IQCSamplePlan;
import com.bosch.binin.api.domain.dto.BinInDTO;
import com.bosch.binin.api.domain.dto.IQCSamplePlanDTO;
import com.bosch.binin.api.domain.dto.IQCSamplePlanQueryDTO;
import com.bosch.binin.api.domain.dto.IQCWareShiftDTO;
import com.bosch.binin.api.domain.vo.BinInVO;
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

    List<IQCSamplePlan> manualAdd(List<IQCSamplePlanDTO> dto);

    void modifySscc(IQCSamplePlanDTO dto);

    IQCSamplePlan binDown(String ssccNb);

    BinInVO getBinInInfo(String sscc);

    BinInVO performBinIn(BinInDTO binInDTO);

    IQCSamplePlan cancel(Long id);

    IQCSamplePlanVO info(String mesBarCode);

    IQCSamplePlan confirm(IQCSamplePlanDTO dto);

    void addShift(IQCWareShiftDTO dto);

    void cancelWareShift(String ssccNb);

    IQCSamplePlan modifyQuantity(String ssccNb, Double quantity);

    List<IQCSamplePlan> issueJob(Long[] ids);
}
