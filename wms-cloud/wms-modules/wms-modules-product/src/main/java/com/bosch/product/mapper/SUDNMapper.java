package com.bosch.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.product.api.domain.SPDN;
import com.bosch.product.api.domain.SUDN;
import com.bosch.product.api.domain.dto.SPDNDTO;
import com.bosch.product.api.domain.dto.SUDNDTO;
import com.bosch.product.api.domain.vo.SPDNVO;
import com.bosch.product.api.domain.vo.SUDNVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-06-12 12:45
 **/
@Mapper
public interface SUDNMapper extends BaseMapper<SUDN> {

    List<SUDNVO> getList(SUDNDTO sudndto);

    List<SUDNVO> getUnFinishedSUDN();

    List<SUDNVO> getFinishedSUDN();

    List<SUDNVO> getUnFinishedShipSUDN();

    List<SUDNVO> getFinishedShipSUDN();
}
