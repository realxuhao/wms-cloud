package com.bosch.binin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.binin.api.domain.dto.WeightDTO;
import com.bosch.binin.api.enumeration.WeightStatusEnum;
import com.bosch.binin.mapper.WeightMapper;
import com.bosch.binin.service.IWeightService;
import com.bosch.storagein.api.domain.Weight;
import com.ruoyi.common.core.exception.ServiceException;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-05-05 09:22
 **/
@Service
public class WeightServiceImpl extends ServiceImpl<WeightMapper, Weight> implements IWeightService {

    @Override
    public void addWeight(WeightDTO weightDTO) {
        Weight weight = new Weight();
        weight.setIp(weightDTO.getIp());
        weight.setPort(weightDTO.getPort());
        weight.setUploadTime(new Date());
        weight.setWeight(weightDTO.getTotalWeight());
        weight.setStatus(WeightStatusEnum.NOT_USE.code());
        this.save(weight);
    }

    @Override
    public Weight getByPort(String ip) {
        LambdaQueryWrapper<Weight> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Weight::getIp, ip);
//        queryWrapper.eq(Weight::getStatus,WeightStatusEnum.NOT_USE.code());
        queryWrapper.orderByDesc(Weight::getUploadTime);
        queryWrapper.last("limit 1");
        Weight weight = this.getOne(queryWrapper);
//        if (weight == null || weight.getStatus() != WeightStatusEnum.NOT_USE.code()) {
//            throw new ServiceException("无可用称重数据");
//        }

        return weight;
    }
}
