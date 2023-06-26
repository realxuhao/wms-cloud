package com.bosch.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.product.api.domain.ProductStock;
import com.bosch.product.api.domain.SPDN;
import com.bosch.product.api.domain.SUDN;
import com.bosch.product.api.domain.dto.SPDNDTO;
import com.bosch.product.api.domain.dto.SUDNDTO;
import com.bosch.product.api.domain.enumeration.SUDNStatusEnum;
import com.bosch.product.api.domain.vo.SPDNVO;
import com.bosch.product.api.domain.vo.SUDNVO;
import com.bosch.product.mapper.SPDNMapper;
import com.bosch.product.mapper.SUDNMapper;
import com.bosch.product.service.IProductStockService;
import com.bosch.product.service.ISPDNService;
import com.bosch.product.service.ISUDNService;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.enums.QualityStatusEnums;
import com.ruoyi.common.core.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-06-12 12:55
 **/
@Service
public class SUDNServiceImpl extends ServiceImpl<SUDNMapper, SUDN>
        implements ISUDNService {

    @Autowired
    private SUDNMapper sudnMapper;


    @Override
    public List<SUDNVO> getList(SUDNDTO sudndto) {
        return sudnMapper.getList(sudndto);
    }

    @Override
    public void batchDelete(List<Long> ids) {
        List<SUDN> sudns = this.listByIds(ids);
        sudns.stream().filter(sudn -> sudn.getStatus()== SUDNStatusEnum.WAITING_GEN.code());
    }

    @Override
    public void generate(List<Long> idsList) {

    }



}
