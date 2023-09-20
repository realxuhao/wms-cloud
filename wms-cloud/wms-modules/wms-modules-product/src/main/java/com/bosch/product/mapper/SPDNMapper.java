package com.bosch.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.product.api.domain.SPDN;
import com.bosch.product.api.domain.ShippingTask;
import com.bosch.product.api.domain.dto.SPDNDTO;
import com.bosch.product.api.domain.vo.SPDNCount;
import com.bosch.product.api.domain.vo.SPDNVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-06-12 12:45
 **/
@Mapper
public interface SPDNMapper extends BaseMapper<SPDN> {

    List<SPDNVO> getList(SPDNDTO spdndto);

    SPDNCount getSPDNCount(SPDNDTO spdndto);

}
