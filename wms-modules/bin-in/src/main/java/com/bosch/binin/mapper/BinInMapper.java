package com.bosch.binin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.binin.domain.BinIn;
import com.bosch.binin.domain.dto.BinInQueryDTO;
import com.bosch.binin.domain.vo.BinInVO;
import com.bosch.masterdata.api.domain.Area;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: UWH4SZH
 * @since: 10/19/2022 10:16
 * @description:
 */

@Mapper
@Repository("binInMapper")
public interface BinInMapper extends BaseMapper<BinIn> {

    List<BinInVO> selectBinVOList(BinInQueryDTO queryDTO);

}
