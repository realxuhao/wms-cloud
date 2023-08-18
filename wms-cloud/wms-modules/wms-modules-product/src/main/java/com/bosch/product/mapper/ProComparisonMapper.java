package com.bosch.product.mapper;

import com.bosch.product.api.domain.ProComparison;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.product.api.domain.dto.ProComparisonDTO;
import com.bosch.product.api.domain.vo.ProComparisonVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author GUZ1CGD4
* @description 针对表【pro_comparison(成品对比表)】的数据库操作Mapper
* @createDate 2023-05-12 13:16:48
* @Entity com.bosch.product.api.domain.ProComparison
*/
public interface ProComparisonMapper extends BaseMapper<ProComparison> {

    List<ProComparisonVO> getList (@Param("dto") ProComparisonDTO dto, @Param("userName") String userName);
}




