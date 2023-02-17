package com.bosch.masterdata.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.masterdata.api.domain.Ecn;
import com.bosch.masterdata.api.domain.dto.EcnDTO;
import com.bosch.masterdata.api.domain.vo.EcnVO;
import com.bosch.masterdata.api.enumeration.*;
import com.bosch.masterdata.mapper.EcnMapper;
import com.bosch.masterdata.mapper.NmdMapper;
import com.bosch.masterdata.service.IEcnService;
import com.bosch.masterdata.utils.BeanConverUtil;
import com.ruoyi.common.core.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author GUZ1CGD4
* @description 针对表【md_ecn(ecn主数据)】的数据库操作Service实现
* @createDate 2023-02-17 11:20:47
*/
@Service
public class EcnServiceImpl extends ServiceImpl<EcnMapper, Ecn>
    implements IEcnService {

    @Autowired
    private EcnMapper ecnMapper;

    @Override
    public List<EcnVO> selectList(EcnDTO ecnDTO) {
        List<EcnVO> ecnVOS = ecnMapper.selectList(ecnDTO);
        return ecnVOS;
    }


    @Override
    public EcnVO selectEcnById(Long id) {
        Ecn ecn = ecnMapper.selectById(id);
        EcnVO conver = BeanConverUtil.conver(ecn, EcnVO.class);
        return conver;
    }

    @Override
    public Integer insertEcn(EcnDTO ecnDTO) {
        List<EcnDTO> list=new ArrayList<>();
        list.add(ecnDTO);
        validEcnList(list);
        Ecn ecn = BeanConverUtil.conver(ecnDTO, Ecn.class);
        int insert = ecnMapper.insert(ecn);
        return insert;
    }

    @Override
    public Integer updateEcn(EcnDTO ecnDTO) {
        return null;
    }

    @Override
    public Integer deleteEcn(Long[] ids) {
        return ecnMapper.deleteEcn(ids);
    }


    @Override
    public boolean validEcnList(List<EcnDTO> ecnDTOS) {
        return ecnMapper.validateRecord(ecnDTOS)>0;
    }

    @Override
    public boolean validData(List<EcnDTO> ecnDTOS) {
        ecnDTOS.forEach(r->{
            //校验类别
            if(r.getClassification()==null){
                throw  new ServiceException("校验类别为空");
            }
            boolean checkClass = EcnClassificationEnum.contain(r.getClassification());
            if (!checkClass){
                throw  new ServiceException("校验类别不规范");
            }

            //校验TTS取样规则
            if(r.getClassification().equals(EcnClassificationEnum.TTS.getDesc())&& r.getPlan()==null){
                throw  new ServiceException("类别为TTS时TTS取样规则不能为空");
            }
            boolean checkPlan = EcnPlanEnum.contain(r.getPlan().toString());
            if (!checkPlan){
                throw  new ServiceException("TTS取样规则不规范");
            }
        });

        return true;
    }
}




