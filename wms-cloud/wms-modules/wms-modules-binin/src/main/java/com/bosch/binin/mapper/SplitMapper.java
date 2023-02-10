package com.bosch.binin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.binin.api.domain.SplitRecord;
import com.bosch.binin.api.domain.dto.SplitQuertDTO;
import com.bosch.binin.api.domain.vo.SplitRecordVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-01-17 10:14
 **/
@Mapper
@Repository("splitMapper")
public interface SplitMapper extends BaseMapper<SplitRecord> {

    List<SplitRecordVO> getList(SplitQuertDTO queryDTO);
}
