package com.bosch.binin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.binin.api.domain.MaterialCall;
import com.bosch.binin.api.domain.dto.MaterialCallDTO;
import com.bosch.binin.api.domain.dto.MaterialCallQueryDTO;
import com.bosch.binin.api.domain.vo.MaterialCallVO;
import com.bosch.binin.mapper.MaterialCallMapper;
import com.bosch.binin.service.IMaterialCallService;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.utils.bean.BeanConverUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-11-10 14:12
 **/
@Service
public class MaterialCallServiceImpl extends ServiceImpl<MaterialCallMapper, MaterialCall> implements IMaterialCallService {


    @Autowired
    private MaterialCallMapper materialCallMapper;

    @Override
    public List<MaterialCallVO> getMaterialCallList(MaterialCallQueryDTO queryDTO) {

        LambdaQueryWrapper<MaterialCall> queryWrapper = new LambdaQueryWrapper();
        if (queryDTO != null) {
            queryWrapper.eq(StringUtils.isNotEmpty(queryDTO.getMaterialNb()), MaterialCall::getMaterialNb, queryDTO.getMaterialNb())
                    .eq(StringUtils.isNotEmpty(queryDTO.getCell()), MaterialCall::getCell, queryDTO.getCell())
                    .eq(StringUtils.isNotEmpty(queryDTO.getOrderNb()), MaterialCall::getOrderNb, queryDTO.getOrderNb())
                    .apply(ObjectUtils.allNotNull(queryDTO.getStartCreateTime()), "date_format (create_time,'%Y-%m-%d') >= date_format ({0},'%Y-%m-%d')", queryDTO.getStartCreateTime())
                    .apply(ObjectUtils.allNotNull(queryDTO.getEndCreateTime()), "date_format (create_time,'%Y-%m-%d') <= date_format ({0},'%Y-%m-%d')", queryDTO.getEndCreateTime());
        }
        List<MaterialCall> materialCalls = materialCallMapper.selectList(queryWrapper);
        List<MaterialCallVO> materialCallVOS = BeanConverUtil.converList(materialCalls, MaterialCallVO.class);
        return materialCallVOS;
    }


    @Override
    public boolean validList( List<MaterialCallDTO> dtos) {
        List<String> orderNbs = dtos.stream().map(MaterialCallDTO::getOrderNb).collect(Collectors.toList());
        LambdaQueryWrapper<MaterialCall> wrapper=new LambdaQueryWrapper<MaterialCall>();
        wrapper.in(MaterialCall::getOrderNb,orderNbs);
//        dtos.forEach(r->{
//            wrapper.or(wp->wp.eq(MaterialCall::getOrderNb,r.getOrderNb()).eq(MaterialCall::getMaterialNb,r.getMaterialNb()));
//        });
        Integer integer = materialCallMapper.selectCount(wrapper);

        return  integer>0;
    }
}
