package com.bosch.binin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.binin.api.domain.BinIn;
import com.bosch.binin.api.domain.MaterialCall;
import com.bosch.binin.api.domain.dto.MaterialCallQueryDTO;
import com.bosch.binin.api.domain.vo.MaterialCallVO;
import com.bosch.binin.mapper.BinInMapper;
import com.bosch.binin.mapper.MaterialCallMapper;
import com.bosch.binin.service.IMaterialCallService;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.utils.bean.BeanConverUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-11-10 14:12
 **/
public class MaterialCallServiceImpl extends ServiceImpl<MaterialCallMapper, MaterialCall> implements IMaterialCallService {


    @Autowired
    private MaterialCallMapper materialCallMapper;

    @Override
    public List<MaterialCallVO> getMaterialCallList(MaterialCallQueryDTO queryDTO) {

        LambdaQueryWrapper<MaterialCall> queryWrapper = new LambdaQueryWrapper();
        if (queryDTO != null) {
            queryWrapper.eq(StringUtils.isNotEmpty(queryDTO.getMaterialNb()), MaterialCall::getMaterialNb, queryDTO.getMaterialNb())
                    .eq(StringUtils.isNotEmpty(queryDTO.getCell()), MaterialCall::getCell, queryDTO.getCell())
                    .eq(StringUtils.isNotEmpty(queryDTO.getOderNb()), MaterialCall::getOderNb, queryDTO.getOderNb())
                    .apply(ObjectUtils.allNotNull(queryDTO.getStartCreateTime()), "date_format (create_time,'%Y-%m-%d') >= date_format ({0},'%Y-%m-%d')", queryDTO.getStartCreateTime())
                    .apply(ObjectUtils.allNotNull(queryDTO.getStartCreateTime()), "date_format (create_time,'%Y-%m-%d') <= date_format ({0},'%Y-%m-%d')", queryDTO.getEndCreateDate());
        }
        List<MaterialCall> materialCalls = materialCallMapper.selectList(queryWrapper);
        List<MaterialCallVO> materialCallVOS = BeanConverUtil.converList(materialCalls, MaterialCallVO.class);
        return materialCallVOS;
    }
}
