package com.bosch.masterdata.service.impl;

import com.bosch.masterdata.api.domain.MissionMap;
import com.bosch.masterdata.api.domain.MissionToDo;
import com.bosch.masterdata.api.domain.ReportMaterial;
import com.bosch.masterdata.api.domain.ReportWareShift;
import com.bosch.masterdata.api.domain.dto.ReportBinDTO;
import com.bosch.masterdata.api.domain.dto.ReportWareShiftDTO;
import com.bosch.masterdata.api.domain.vo.ReportBinVO;
import com.bosch.masterdata.mapper.ReportMapper;
import com.bosch.masterdata.service.IReportService;
import com.ruoyi.common.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
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
        List<ReportBinVO> reportBinVOS = new ArrayList<>();
        if(reportBinDTO.getType()!=null&&("0").equals(reportBinDTO.getType().toString())){
            reportBinVOS= reportMapper.selectCellReportByType(reportBinDTO);
        }
        if(reportBinDTO.getType()!=null&&("1").equals(reportBinDTO.getType().toString())){
            reportBinVOS= reportMapper.selectCellReportByMonth(reportBinDTO);
            if (!CollectionUtils.isEmpty(reportBinVOS)){
                for (ReportBinVO reportBinVO : reportBinVOS) {
                    int mb = reportBinVO.getMaterialOccupyBin() != null ? reportBinVO.getMaterialOccupyBin() : 0;
                    int pb = reportBinVO.getProductOccupyBin() != null ? reportBinVO.getProductOccupyBin() : 0;
                    BigDecimal used = new BigDecimal(mb+pb);
                    BigDecimal all = new BigDecimal(reportBinVO.getTotalBin()!= null ? reportBinVO.getTotalBin() : 0);
                    BigDecimal divide = used.divide(all, 2, BigDecimal.ROUND_HALF_UP);
                    reportBinVO.setPercent(divide);
                }
            }
        }
        return reportBinVOS;
    }

    @Override
    public List<ReportMaterial> oldMaterial() {
        List<ReportMaterial> reportMaterials = reportMapper.oldMaterial();
//        //循环reportMaterials，根据batchNb去重,根据createTime排序
//        Map<String, List<ReportMaterial>> collect = reportMaterials.stream().collect(Collectors.groupingBy(ReportMaterial::getBatchNb));
//        //将collect的value取出来，组合成新list
//        List<ReportMaterial> list=new ArrayList<>();
//        for (Map.Entry<String, List<ReportMaterial>> entry : collect.entrySet()) {
//            List<ReportMaterial> value = entry.getValue();
//            ReportMaterial reportMaterial = value.get(0);
//            list.add(reportMaterial);
//        }
        return reportMaterials;
    }

    @Override
    public List<ReportMaterial> expiredMaterial() {
        //过期30天物料
        List<ReportMaterial> reportMaterials = reportMapper.expiredMaterial();

        return reportMaterials;
    }

    @Override
    public List<ReportWareShift> reportWareShift(ReportWareShiftDTO reportWareShiftDTO) {
        List<ReportWareShift> reportWareShifts = reportMapper.reportWareShift(reportWareShiftDTO);
        return reportWareShifts;
    }

    @Override
    public List<MissionToDo> selectReportList(MissionToDo mission) {
        List<String> codes=new ArrayList<>();
        List<MissionToDo> list=new ArrayList<>();
        //获取仓库或者cell
        if (StringUtils.isNotEmpty(mission.getCell())){
            if (!mission.getCell().equals("All")){
                codes.add(mission.getCell());
            }else {
                codes= reportMapper.getCellCode();
            }
            for (String s : codes) {
                MissionToDo missionToDo = new MissionToDo();
                missionToDo.setCell(s);
                list.add(missionToDo);
            }


        }else if(StringUtils.isNotEmpty(mission.getWareCode())){
            if (!mission.getWareCode().equals("All")){
                codes.add(mission.getWareCode());
            }else {
                codes= reportMapper.getWareCode();
            }

            for (String s : codes) {
                MissionToDo missionToDo = new MissionToDo();
                missionToDo.setWareCode(s);
                list.add(missionToDo);
            }
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
            if (StringUtils.isNotEmpty(mission.getCell())){
                missionToDo.setToBeReceived(receiveMap.get(missionToDo.getCell()));
                missionToDo.setToBeBin(beBinMap.get(missionToDo.getCell()));
                missionToDo.setToBeCall(beCallMap.get(missionToDo.getCell()));
                missionToDo.setToBeMove(beMoveMap.get(missionToDo.getCell()));
            }else if(StringUtils.isNotEmpty(mission.getWareCode())){
                missionToDo.setToBeReceived(receiveMap.get(missionToDo.getWareCode()));
                missionToDo.setToBeBin(beBinMap.get(missionToDo.getWareCode()));
                missionToDo.setToBeCall(beCallMap.get(missionToDo.getWareCode()));
                missionToDo.setToBeMove(beMoveMap.get(missionToDo.getWareCode()));
            }

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
