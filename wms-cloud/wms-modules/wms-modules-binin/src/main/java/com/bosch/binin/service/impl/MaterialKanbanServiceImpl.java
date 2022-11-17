package com.bosch.binin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.binin.api.domain.BinIn;
import com.bosch.binin.api.domain.MaterialKanban;
import com.bosch.binin.api.domain.dto.MaterialKanbanDTO;
import com.bosch.binin.api.domain.vo.MaterialKanbanVO;
import com.bosch.binin.mapper.BinInMapper;
import com.bosch.binin.mapper.MaterialKanbanMapper;
import com.bosch.binin.service.IBinInService;
import com.bosch.binin.service.IMaterialKanbanService;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.utils.bean.BeanConverUtil;
import com.ruoyi.common.core.web.page.PageDomain;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MaterialKanbanServiceImpl extends ServiceImpl<MaterialKanbanMapper, MaterialKanban> implements IMaterialKanbanService {



        @Resource
        MaterialKanbanMapper materialKanbanMapper;

        @Override
        public IPage<MaterialKanbanVO> pageList(MaterialKanbanDTO dto) {
            IPage<MaterialKanban> page= new Page<>();
            if (dto.getPageNum()!=null&& dto.getPageSize()!=null){
                page= new Page<>(dto.getPageNum(), dto.getPageSize());
            }
            //查询条件
            LambdaQueryWrapper<MaterialKanban> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.like(StringUtils.isNotEmpty(dto.getOrderNumber()), MaterialKanban::getOrderNumber,dto.getOrderNumber());
            lambdaQueryWrapper.like(StringUtils.isNotEmpty(dto.getFactoryCode()), MaterialKanban::getFactoryCode,dto.getFactoryCode());
            lambdaQueryWrapper.like(StringUtils.isNotEmpty(dto.getWareCode()), MaterialKanban::getWareCode,dto.getWareCode());
            lambdaQueryWrapper.like(StringUtils.isNotEmpty(dto.getAreaCode()), MaterialKanban::getAreaCode,dto.getAreaCode());
            lambdaQueryWrapper.like(StringUtils.isNotEmpty(dto.getMaterialCode()), MaterialKanban::getMaterialCode,dto.getMaterialCode());
            lambdaQueryWrapper.like(StringUtils.isNotEmpty(dto.getSsccNumber()), MaterialKanban::getSsccNumber,dto.getSsccNumber());
            lambdaQueryWrapper.like(StringUtils.isNotEmpty(dto.getBinCode()), MaterialKanban::getBinCode,dto.getBinCode());
            lambdaQueryWrapper.like(StringUtils.isNotEmpty(dto.getCell()), MaterialKanban::getCell,dto.getCell());
            lambdaQueryWrapper.like(StringUtils.isNotEmpty(dto.getCreateBy()), MaterialKanban::getCreateBy,dto.getCreateBy());
            lambdaQueryWrapper.ge(dto.getCreateTimeStart()!=null, MaterialKanban::getCreateTime,dto.getCreateTimeStart());
            lambdaQueryWrapper.le(dto.getCreateTimeEnd()!=null, MaterialKanban::getCreateTime,dto.getCreateTimeEnd());
            lambdaQueryWrapper.like(StringUtils.isNotEmpty(dto.getUpdateBy()), MaterialKanban::getUpdateBy,dto.getUpdateBy());
            lambdaQueryWrapper.ge(dto.getUpdateTimeStart()!=null, MaterialKanban::getUpdateTime,dto.getUpdateTimeStart());
            lambdaQueryWrapper.le(dto.getUpdateTimeEnd()!=null, MaterialKanban::getUpdateTime,dto.getUpdateTimeEnd());
            lambdaQueryWrapper.like(dto.getType()!=null, MaterialKanban::getType,dto.getType());
            lambdaQueryWrapper.like(dto.getStatus()!=null, MaterialKanban::getStatus,dto.getStatus());

            IPage<MaterialKanban> materialKanbanIPage = materialKanbanMapper.selectPage(page, lambdaQueryWrapper);
            //mp提供了convert方法,将数据重新封装
           return materialKanbanIPage.convert(u->{
                MaterialKanbanVO v = new MaterialKanbanVO();
                BeanUtils.copyProperties(u, v);//拷贝
                return v;
            });
        }


}

