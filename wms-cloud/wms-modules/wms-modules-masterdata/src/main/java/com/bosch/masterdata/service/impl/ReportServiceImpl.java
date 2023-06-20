package com.bosch.masterdata.service.impl;

import com.bosch.masterdata.api.domain.MissionMap;
import com.bosch.masterdata.api.domain.MissionToDo;
import com.bosch.masterdata.api.domain.dto.ReportBinDTO;
import com.bosch.masterdata.api.domain.vo.ReportBinVO;
import com.bosch.masterdata.mapper.ReportMapper;
import com.bosch.masterdata.service.IReportService;
import com.ruoyi.common.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements IReportService {

    @Autowired
    private ReportMapper reportMapper;
    @Override
    public List<ReportBinVO> selectCellReportByType(ReportBinDTO reportBinDTO) {
        List<ReportBinVO> reportBinVOS = reportMapper.selectCellReportByType(reportBinDTO);
        return reportBinVOS;
    }
    @Override
    public List<MissionToDo> selectReportList(MissionToDo mission) {
        List<String> codes=new ArrayList<>();
        //获取仓库或者cell
        if (StringUtils.isNotEmpty(mission.getCell())){
            codes= reportMapper.getCellCode();
        }else if(StringUtils.isNotEmpty(mission.getWareCode())){
            codes= reportMapper.getWareCode();
        }

        List<MissionToDo> list=new ArrayList<>();
        for (String s : codes) {
            MissionToDo missionToDo = new MissionToDo();
            missionToDo.setCell(s);
            list.add(missionToDo);
        }
        if (CollectionUtils.isEmpty(list)){
            return list;
        }
        List<MissionMap> beReceived = reportMapper.toBeReceived(mission.getCell(), mission.getWareCode());
        List<MissionMap> beCall = reportMapper.toBeCall(mission.getCell(), mission.getWareCode());
        List<MissionMap> beBin = reportMapper.toBeBin(mission.getCell(), mission.getWareCode());
        List<MissionMap> beMove = reportMapper.toBeMove(mission.getCell(), mission.getWareCode());
        //待入库map
        Map<String, Integer> receiveMap = getMap(beReceived);
        //上架map
        Map<String, Integer> beBinMap = getMap(beBin);
        //叫料map
        Map<String, Integer> beCallMap = getMap(beCall);
        //移库map
        Map<String, Integer> beMoveMap = getMap(beMove);
        //赋值list
        for (MissionToDo missionToDo : list) {
            missionToDo.setToBeReceived(receiveMap.get(missionToDo.getCell()));
            missionToDo.setToBeBin(beBinMap.get(missionToDo.getCell()));
            missionToDo.setToBeCall(beCallMap.get(missionToDo.getCell()));
            missionToDo.setToBeMove(beMoveMap.get(missionToDo.getCell()));
        }
        return list;
    }



    //获取map
    public Map<String, Integer> getMap(List<MissionMap> list){
        if (CollectionUtils.isEmpty(list)){
            Map<String, Integer> map=new HashMap<>();
            return map;
        }
        List<MissionMap> filterNull = list.stream().filter(r -> r.getCode() != null).collect(Collectors.toList());
        Map<String, Integer> map = filterNull.stream().collect(Collectors.toMap(MissionMap::getCode, MissionMap::getNumber));
        return map;
    }
}
