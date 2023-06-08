package com.bosch.masterdata.service.impl;

import com.bosch.masterdata.api.domain.MissionMap;
import com.bosch.masterdata.api.domain.MissionToDo;
import com.bosch.masterdata.mapper.ReportMapper;
import com.bosch.masterdata.service.IReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements IReportService {

    @Autowired
    private ReportMapper reportMapper;
    @Override
    public List<MissionToDo> selectReportList(MissionToDo mission) {
        List<String> cellCode = reportMapper.getCellCode();
        List<MissionToDo> list=new ArrayList<>();
        for (String s : cellCode) {
            MissionToDo missionToDo = new MissionToDo();
            missionToDo.setCell(s);
            list.add(missionToDo);
        }
        if (CollectionUtils.isEmpty(list)){
            return list;
        }
        List<MissionMap> beReceived = reportMapper.toBeReceived();
        List<MissionMap> filterNull = beReceived.stream().filter(r -> r.getCode() != null).collect(Collectors.toList());
        Map<String, Integer> receiveMap = filterNull.stream().collect(Collectors.toMap(MissionMap::getCode, MissionMap::getNumber));

        //赋值list
        for (MissionToDo missionToDo : list) {
            missionToDo.setToBeReceived(receiveMap.get(missionToDo.getCell()));
        }
        return list;
    }
}
