package com.bosch.binin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.binin.api.domain.RegisterBom;
import com.bosch.binin.api.domain.dto.RegisterBomDTO;
import com.bosch.binin.mapper.RegisterBomMapper;;
import com.bosch.binin.service.IRegisterBomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegisterBomServiceImpl extends ServiceImpl<RegisterBomMapper, RegisterBom> implements IRegisterBomService {

    @Autowired
    private RegisterBomMapper registerBomMapper;

    @Override
    public boolean validList(String orderNb) {
        //List<String> orderNbs = dtos.stream().map(RegisterBomDTO::getOrderNumber).collect(Collectors.toList());
        LambdaQueryWrapper<RegisterBom> wrapper = new LambdaQueryWrapper<RegisterBom>();
        wrapper.eq(RegisterBom::getOrderNumber, orderNb);
        wrapper.eq(RegisterBom::getDeleteFlag, 0);
        Integer integer = registerBomMapper.selectCount(wrapper);
        return integer > 0;
    }

    @Override
    public Integer deleteList(String orderNb) {
        //List<String> orderNbs = dtos.stream().map(RegisterBomDTO::getOrderNumber).collect(Collectors.toList());
        LambdaQueryWrapper<RegisterBom> wrapper = new LambdaQueryWrapper<RegisterBom>();
        wrapper.eq(RegisterBom::getOrderNumber, orderNb);
        wrapper.eq(RegisterBom::getDeleteFlag, 0);
        Integer integer = registerBomMapper.delete(wrapper);
        return integer;
    }

}
