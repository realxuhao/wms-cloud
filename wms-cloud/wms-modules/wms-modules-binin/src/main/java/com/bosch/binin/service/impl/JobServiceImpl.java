package com.bosch.binin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bosch.binin.api.domain.IQCSamplePlan;
import com.bosch.binin.api.domain.MaterialKanban;
import com.bosch.binin.api.domain.WareShift;
import com.bosch.binin.api.domain.vo.JobVO;
import com.bosch.binin.api.enumeration.IQCStatusEnum;
import com.bosch.binin.api.enumeration.KanbanStatusEnum;
import com.bosch.binin.service.*;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-08-09 10:08
 **/
@Service
public class JobServiceImpl implements IJobService {

    @Autowired
    private IStockService stockService;

    @Autowired
    private IMaterialKanbanService kanbanService;

    @Autowired
    private IIQCSamplePlanService iqcSamplePlanService;

    @Autowired
    private IWareShiftService wareShiftService;


    @Override
    public void validStockStatus(String ssccNb) {

        //查询看板
        LambdaQueryWrapper<MaterialKanban> kanbanLambdaQueryWrapper = new LambdaQueryWrapper<>();
        kanbanLambdaQueryWrapper.eq(MaterialKanban::getSsccNumber, ssccNb);
        kanbanLambdaQueryWrapper.eq(MaterialKanban::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        kanbanLambdaQueryWrapper.ne(MaterialKanban::getStatus, KanbanStatusEnum.CANCEL.value());
        kanbanLambdaQueryWrapper.ne(MaterialKanban::getStatus, KanbanStatusEnum.FINISH.value());
        kanbanLambdaQueryWrapper.ne(MaterialKanban::getStatus, KanbanStatusEnum.LINE_RECEIVED.value());

        kanbanLambdaQueryWrapper.last("for update ");
        MaterialKanban materialKanban = kanbanService.getOne(kanbanLambdaQueryWrapper);
        if (materialKanban != null) {
            throw new ServiceException("该SSCC" + ssccNb + "存在捡配任务," + "状态为:" + KanbanStatusEnum.getDesc(String.valueOf(materialKanban.getStatus())));
        }

        //查询移库
        LambdaQueryWrapper<WareShift> shiftQueryWrapper = new LambdaQueryWrapper<>();
        shiftQueryWrapper.eq(WareShift::getSsccNb, ssccNb);
        shiftQueryWrapper.ne(WareShift::getStatus, KanbanStatusEnum.FINISH.value());
        shiftQueryWrapper.ne(WareShift::getStatus, KanbanStatusEnum.CANCEL.value());
        shiftQueryWrapper.ne(WareShift::getStatus, KanbanStatusEnum.LINE_RECEIVED.value());
        shiftQueryWrapper.last("for update");
        WareShift wareShift = wareShiftService.getOne(shiftQueryWrapper);
        if (wareShift != null) {
            throw new ServiceException("该SSCC" + ssccNb + "存在移库任务," + "状态为:" + KanbanStatusEnum.getDesc(String.valueOf(wareShift.getStatus())));
        }
        //查询IQC
        LambdaQueryWrapper<IQCSamplePlan> iqcQueryWrapper = new LambdaQueryWrapper<>();
        iqcQueryWrapper.eq(IQCSamplePlan::getSsccNb, ssccNb);
        iqcQueryWrapper.eq(IQCSamplePlan::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        iqcQueryWrapper.ne(IQCSamplePlan::getStatus, IQCStatusEnum.CANCEL.code());
        iqcQueryWrapper.ne(IQCSamplePlan::getStatus, IQCStatusEnum.FINISH.code());
        iqcQueryWrapper.last("for update");
        IQCSamplePlan samplePlan = iqcSamplePlanService.getOne(iqcQueryWrapper);
        if (samplePlan != null) {
            throw new ServiceException("该SSCC" + ssccNb + "存在IQC抽样任务," + "状态为:" + IQCStatusEnum.getDesc(samplePlan.getStatus()));
        }

    }

    @Override
    public JobVO getJobDescBySSCC(String ssccNb) {

        //查询看板
        LambdaQueryWrapper<MaterialKanban> kanbanLambdaQueryWrapper = new LambdaQueryWrapper<>();
        kanbanLambdaQueryWrapper.eq(MaterialKanban::getSsccNumber, ssccNb);
        kanbanLambdaQueryWrapper.eq(MaterialKanban::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        kanbanLambdaQueryWrapper.ne(MaterialKanban::getStatus, KanbanStatusEnum.CANCEL.value());
        kanbanLambdaQueryWrapper.ne(MaterialKanban::getStatus, KanbanStatusEnum.FINISH.value());
        kanbanLambdaQueryWrapper.ne(MaterialKanban::getStatus, KanbanStatusEnum.LINE_RECEIVED.value());

        kanbanLambdaQueryWrapper.last("for update ");
        MaterialKanban materialKanban = kanbanService.getOne(kanbanLambdaQueryWrapper);
        if (materialKanban != null) {
            JobVO jobVO = new JobVO();
            jobVO.setJobDesc("捡配任务");
            jobVO.setStatusDesc(KanbanStatusEnum.getDesc(String.valueOf(materialKanban.getStatus())));
            return jobVO;
        }

        //查询移库
        LambdaQueryWrapper<WareShift> shiftQueryWrapper = new LambdaQueryWrapper<>();
        shiftQueryWrapper.eq(WareShift::getSsccNb, ssccNb);
        shiftQueryWrapper.ne(WareShift::getStatus, KanbanStatusEnum.FINISH.value());
        shiftQueryWrapper.ne(WareShift::getStatus, KanbanStatusEnum.CANCEL.value());
        shiftQueryWrapper.ne(WareShift::getStatus, KanbanStatusEnum.LINE_RECEIVED.value());
        shiftQueryWrapper.last("for update");
        WareShift wareShift = wareShiftService.getOne(shiftQueryWrapper);
        if (wareShift != null) {
            JobVO jobVO = new JobVO();
            jobVO.setJobDesc("移库任务");
            jobVO.setStatusDesc(KanbanStatusEnum.getDesc(String.valueOf(wareShift.getStatus())));
            return jobVO;
        }


        //查询IQC
        LambdaQueryWrapper<IQCSamplePlan> iqcQueryWrapper = new LambdaQueryWrapper<>();
        iqcQueryWrapper.eq(IQCSamplePlan::getSsccNb, ssccNb);
        iqcQueryWrapper.eq(IQCSamplePlan::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        iqcQueryWrapper.ne(IQCSamplePlan::getStatus, IQCStatusEnum.CANCEL.code());
        iqcQueryWrapper.ne(IQCSamplePlan::getStatus, IQCStatusEnum.FINISH.code());
        iqcQueryWrapper.last("for update");
        IQCSamplePlan samplePlan = iqcSamplePlanService.getOne(iqcQueryWrapper);
        if (samplePlan != null) {
            JobVO jobVO = new JobVO();
            jobVO.setJobDesc("IQC抽样任务");
            jobVO.setStatusDesc(IQCStatusEnum.getDesc(samplePlan.getStatus()));
            return jobVO;

        }

        JobVO jobVO = new JobVO();
        jobVO.setJobDesc("无任务");
        return jobVO;

    }
}
