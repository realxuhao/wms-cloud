package com.bosch.binin.service;

import com.bosch.binin.api.domain.MaterialCall;
import com.bosch.binin.api.domain.dto.StockQueryDTO;
import com.bosch.binin.api.domain.vo.MaterialCallVO;
import com.bosch.binin.api.domain.vo.StockVO;

import java.util.List;

/**
 * @program: wms-cloud
 * @description: 叫料service
 * @author: xuhao
 * @create: 2022-11-09 14:45
 **/
public interface IMaterialCallService {
    List<MaterialCallVO> selectStockVOList(List<MaterialCall> materialCalls);

}
