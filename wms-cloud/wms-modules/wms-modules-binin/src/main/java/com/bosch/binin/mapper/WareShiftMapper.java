package com.bosch.binin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.binin.api.domain.WareShift;
import com.bosch.binin.api.domain.dto.WareShiftQueryDTO;
import com.bosch.binin.api.domain.vo.WareShiftVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-11-16 20:46
 **/
@Mapper
@Repository("wareShiftMapper")
public interface WareShiftMapper extends BaseMapper<WareShift> {

    List<WareShiftVO> getWaitingBinIn(String wareCode);

    List<WareShiftVO> list(WareShiftQueryDTO queryDTO);
}
