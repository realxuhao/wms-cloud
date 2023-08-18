package com.bosch.product.service;

import com.bosch.product.api.domain.ProComparison;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.product.api.domain.dto.ProComparisonDTO;
import com.bosch.product.api.domain.vo.ProComparisonVO;

import java.util.List;

/**
* @author GUZ1CGD4
* @description 针对表【pro_comparison(成品对比表)】的数据库操作Service
* @createDate 2023-05-12 13:16:48
*/
public interface IProComparisonService extends IService<ProComparison> {

   List<ProComparisonVO> getList(ProComparisonDTO dto);

   boolean updateByIdList(List<String> ids);

   boolean deleteByIdList(List<String> ids);
   boolean deleteProComparisonByCreat();
}
