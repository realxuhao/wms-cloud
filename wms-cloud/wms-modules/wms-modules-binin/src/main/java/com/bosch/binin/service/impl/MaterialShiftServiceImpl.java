package com.bosch.binin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.binin.api.domain.MaterialKanban;
import com.bosch.binin.api.domain.WareShift;
import com.bosch.binin.api.domain.dto.AddShiftTaskDTO;
import com.bosch.binin.mapper.MaterialShiftMapper;
import com.bosch.binin.service.IMaterialKanbanService;
import com.bosch.binin.service.IMaterialShiftService;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-11-16 20:48
 **/
@Service
public class MaterialShiftServiceImpl extends ServiceImpl<MaterialShiftMapper, WareShift> implements IMaterialShiftService {


    @Autowired
    private IMaterialKanbanService kanbanService;


    @Override
    public void addShiftRequirement(AddShiftTaskDTO dto) {
        List<String> ssccNbList = dto.getSsccNbList();
        if (CollectionUtils.isEmpty(ssccNbList)) {
            return;
        }

        //先去再次校验一下库存


        //先找出来有job的
        LambdaQueryWrapper<MaterialKanban> kanbanLambdaQueryWrapper = new LambdaQueryWrapper<>();
        kanbanLambdaQueryWrapper.in(MaterialKanban::getSsccNumber, ssccNbList);
        kanbanLambdaQueryWrapper.eq(MaterialKanban::getDeleteFlag, DeleteFlagStatus.FALSE);
        kanbanLambdaQueryWrapper.select(MaterialKanban::getSsccNumber);
        List<MaterialKanban> list = kanbanService.list(kanbanLambdaQueryWrapper);
        List<String> jobSsccList = list.stream().map(MaterialKanban::getSsccNumber).collect(Collectors.toList());



    }
}
