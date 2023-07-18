package com.bosch.product.service;

import com.bosch.binin.api.domain.vo.StockVO;
import com.bosch.product.api.domain.ProComparison;
import com.bosch.product.api.domain.RmComparison;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.product.api.domain.dto.RmComparisonDTO;
import com.bosch.product.api.domain.vo.RmComparisonVO;

import java.text.ParseException;
import java.util.List;

/**
* @author GUZ1CGD4
* @description 针对表【rm_comparison(原材料库存对比)】的数据库操作Service
* @createDate 2023-04-27 13:41:29
*/
public interface IRmComparisonService extends IService<RmComparison> {
    //获取库存信息列表
    List<RmComparison> getRmComparisonVOList(RmComparisonDTO rmComparisonDTO);

    List<RmComparison>  insertRmComparison(List<RmComparison> rmComparisons);

    boolean updateBySsccList(List<String> ssccList);


    List<ProComparison>  insertProComparison (List<ProComparison> proComparisons) ;

    boolean deleteRmComparisonByCreat();
}
