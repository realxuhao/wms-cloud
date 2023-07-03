package com.bosch.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.binin.api.domain.BinIn;
import com.bosch.binin.api.domain.ManualTransferOrder;

import com.bosch.product.mapper.ManualTransferOrderMapper;
import com.bosch.product.service.IManualTransferOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-12-20 13:48
 **/
@Service
public class ManualTransferOrderServiceImpl extends ServiceImpl<ManualTransferOrderMapper, ManualTransferOrder> implements IManualTransferOrderService {



}
