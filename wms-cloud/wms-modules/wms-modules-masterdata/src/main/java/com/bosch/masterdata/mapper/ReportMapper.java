package com.bosch.masterdata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.masterdata.api.domain.Area;
import com.bosch.masterdata.api.domain.MissionMap;
import com.bosch.masterdata.api.domain.dto.AreaDTO;
import com.bosch.masterdata.api.domain.vo.AreaVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;


@Mapper
public interface ReportMapper
{
    List<String> getCellCode();
    List<MissionMap> toBeReceived();
    List<MissionMap> toBeBin();
}
