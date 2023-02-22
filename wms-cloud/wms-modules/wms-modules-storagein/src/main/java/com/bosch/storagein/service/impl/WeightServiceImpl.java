package com.bosch.storagein.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.storagein.api.domain.MaterialReceive;
import com.bosch.storagein.api.domain.Weight;
import com.bosch.storagein.api.enumeration.WeightStatusEnum;
import com.bosch.storagein.mapper.MaterialRecevieMapper;
import com.bosch.storagein.mapper.WeightMapper;
import com.bosch.storagein.service.IMaterialReceiveService;
import com.bosch.storagein.service.IWeightService;
import com.ruoyi.common.core.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-02-17 11:02
 **/
@Service
public class WeightServiceImpl extends ServiceImpl<WeightMapper, Weight> implements IWeightService {

    @Autowired
    private WeightMapper weightMapper;

    @Override
    public void uploadWeight(Weight weight) {
        save(weight);
    }

    @Override
    public Weight getWeightByIP(String ip) {
        LambdaQueryWrapper<Weight> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Weight::getIp, ip);
        queryWrapper.eq(Weight::getStatus, WeightStatusEnum.UNUSE.code());
        queryWrapper.orderByDesc(Weight::getUploadTime);
        queryWrapper.last("limit 1");
        Weight weight = weightMapper.selectOne(queryWrapper);
        if (weight == null) {
            throw new ServiceException(ip + "下暂时没有可用的称重数据");
        }
        return weight;
    }

    @Override
    public Weight getByPort(Integer port) {
        LambdaQueryWrapper<Weight> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Weight::getPort, port);
        queryWrapper.eq(Weight::getStatus, WeightStatusEnum.UNUSE.code());
        queryWrapper.orderByDesc(Weight::getUploadTime);
        queryWrapper.last("limit 1");
        Weight weight = weightMapper.selectOne(queryWrapper);
        if (weight == null) {
            throw new ServiceException(port + "下暂时没有可用的称重数据");
        }
        return weight;
    }


}
