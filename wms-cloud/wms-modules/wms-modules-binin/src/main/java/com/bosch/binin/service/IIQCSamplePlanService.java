package com.bosch.binin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.binin.api.domain.IQCSamplePlan;
import com.bosch.binin.api.domain.ManualTransferOrder;
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

    void manualAdd(List<IQCSamplePlanDTO> dto);

    void modifySscc(IQCSamplePlanDTO dto);

    void binDown(String ssccNb);

    BinInVO getBinInInfo(String sscc);

    void performBinIn(BinInDTO binInDTO);

    void cancel(Long id);

    IQCSamplePlanVO info(String mesBarCode);

    void confirm(IQCSamplePlanDTO dto);

    void addShift(IQCWareShiftDTO dto);
}
