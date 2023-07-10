package com.bosch.masterdata.service.impl;

import com.bosch.masterdata.api.domain.MissionMap;
import com.bosch.masterdata.api.domain.MissionToDo;
import com.bosch.masterdata.api.domain.ReportMaterial;
import com.bosch.masterdata.api.domain.ReportWareShift;
import com.bosch.masterdata.api.domain.dto.EfficiencyDTO;
import com.bosch.masterdata.api.domain.dto.ReportBinDTO;
import com.bosch.masterdata.api.domain.dto.ReportWareShiftDTO;
import com.bosch.masterdata.api.domain.dto.WorkloadDTO;
import com.bosch.masterdata.api.domain.vo.EfficiencyVO;
import com.bosch.masterdata.api.domain.vo.ProcessEfficiencyVO;
import com.bosch.masterdata.api.domain.vo.ReportBinVO;
import com.bosch.masterdata.api.domain.vo.WorkloadVO;
import com.bosch.masterdata.mapper.ReportMapper;
import com.bosch.masterdata.service.IFsmpService;
import com.bosch.masterdata.service.IReportService;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.log.enums.UserOperationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ReportServiceImpl implements IReportService {

    @Autowired
    private ReportMapper reportMapper;

    @Override
    public List<ReportBinVO> selectCellReportByType(ReportBinDTO reportBinDTO) {
        List<ReportBinVO> reportBinVOS = new ArrayList<>();
        if (reportBinDTO.getType() != null && ("0").equals(reportBinDTO.getType().toString())) {
            reportBinVOS = reportMapper.selectCellReportByType(reportBinDTO);
        }
        if (reportBinDTO.getType() != null && ("1").equals(reportBinDTO.getType().toString())) {
            reportBinVOS = reportMapper.selectCellReportByMonth(reportBinDTO);
            if (!CollectionUtils.isEmpty(reportBinVOS)) {
                for (ReportBinVO reportBinVO : reportBinVOS) {
                    int mb = reportBinVO.getMaterialOccupyBin() != null ? reportBinVO.getMaterialOccupyBin() : 0;
                    int pb = reportBinVO.getProductOccupyBin() != null ? reportBinVO.getProductOccupyBin() : 0;
                    BigDecimal used = new BigDecimal(mb + pb);
                    BigDecimal all = new BigDecimal(reportBinVO.getTotalBin() != null ? reportBinVO.getTotalBin() : 0);
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
    public List<WorkloadVO> workload(WorkloadDTO workloadDTO) {
        return reportMapper.workload(workloadDTO);
    }

    @Override
    public List<WorkloadVO> workloadPro(WorkloadDTO workloadDTO) {
        return reportMapper.workloadPro(workloadDTO);
    }

    @Override
    public List<ProcessEfficiencyVO> processEfficiency(EfficiencyDTO efficiencyDTO) {
        List<ProcessEfficiencyVO> processEfficiencyVOS = new ArrayList<>();
        //收货清单导入List
        efficiencyDTO.setOperationType(UserOperationType.Import.getCode());
        List<EfficiencyVO> importList = reportMapper.getEfficiencyByOperationType(efficiencyDTO);
        //上架List
        efficiencyDTO.setOperationType(UserOperationType.MATERIALBININ.getCode());
        List<EfficiencyVO> materialBinInList = reportMapper.getEfficiencyByOperationTwo(efficiencyDTO);

        //IQC下架List
        efficiencyDTO.setOperationType(UserOperationType.IQCBINOUT.getCode());
        List<EfficiencyVO> iqcBinOutList = reportMapper.getEfficiencyByOperationTwo(efficiencyDTO);

        //IQC上架List
        efficiencyDTO.setOperationType(UserOperationType.IQCBININ.getCode());
        List<EfficiencyVO> iqcBinInList = reportMapper.getEfficiencyByOperationType(efficiencyDTO);
        //叫料完成List
        efficiencyDTO.setOperationType(UserOperationType.CALLOVER.getCode());
        List<EfficiencyVO> callOverList = reportMapper.getCallOver(efficiencyDTO);
        for (EfficiencyVO efficiencyVO : callOverList) {
            BigDecimal fm=new BigDecimal(60);
            BigDecimal bwtHours=efficiencyVO.getTimeConsuming().divide(fm,2, BigDecimal.ROUND_HALF_UP);
            efficiencyVO.setTimeConsuming(bwtHours);
        }

        //获取收货清单导入-上架的耗时
        if (!CollectionUtils.isEmpty(importList) && !CollectionUtils.isEmpty(materialBinInList)) {
            List<EfficiencyVO> efficiencyVOS = processEfficiency(importList, materialBinInList);
            if (!CollectionUtils.isEmpty(efficiencyVOS)) {
                 processEfficiencyVOS = getProcessEfficiencyList(efficiencyVOS, 0,processEfficiencyVOS);

            }

        }
        //获取IQC取样：下架-上架的耗时
        if (!CollectionUtils.isEmpty(iqcBinOutList) && !CollectionUtils.isEmpty(iqcBinInList)) {
            List<EfficiencyVO> efficiencyVOS = processEfficiency(iqcBinInList,iqcBinOutList );
            if (!CollectionUtils.isEmpty(efficiencyVOS)) {
                processEfficiencyVOS = getProcessEfficiencyList(efficiencyVOS, 1,processEfficiencyVOS);
            }
        }
        //获取叫料完成时间：同订单号最晚-最早
        if (!CollectionUtils.isEmpty(callOverList)) {
            processEfficiencyVOS = getProcessEfficiencyList(callOverList, 2,processEfficiencyVOS);
        }
        return processEfficiencyVOS;
    }

    //获取俩个List<EfficiencyVO>的耗时
    public List<EfficiencyVO> processEfficiency(List<EfficiencyVO> list1, List<EfficiencyVO> list2) {
        List<EfficiencyVO> newList = new ArrayList<>();
        for (EfficiencyVO vo1 : list1) {
            for (EfficiencyVO vo2 : list2) {
                if (vo1.getSsccNumber().equals(vo2.getSsccNumber())) {
                    //第二个操作在第一个之前的不计算，获取离第一个操作最近的时间
                    if(vo1.getCreateTime().after(vo2.getCreateTime())){
                        continue;
                    }
                    EfficiencyVO newVO = new EfficiencyVO();
                    newVO.setCell(vo1.getCell());
                    newVO.setSsccNumber(vo1.getSsccNumber());
                    newVO.setCreateTime(vo1.getCreateTime());

                    // Calculate time difference
                    BigDecimal timeDiffSeconds = DateUtils.getDifferHour(vo1.getCreateTime(), vo2.getCreateTime());
                    newVO.setTimeConsuming(timeDiffSeconds);

                    newList.add(newVO);
                    break;
                }
            }
        }
        return newList;
    }

    public List<ProcessEfficiencyVO> getProcessEfficiencyList(List<EfficiencyVO> newList, Integer type,List<ProcessEfficiencyVO> vos) {



        Map<String, Map<Date, List<EfficiencyVO>>> groupedData = newList.stream()
                .collect(Collectors.groupingBy(EfficiencyVO::getCell,
                        Collectors.groupingBy(vo -> truncateToDay(vo.getCreateTime()))));
        for (Map.Entry<String, Map<Date, List<EfficiencyVO>>> cellEntry : groupedData.entrySet()) {
            String cell = cellEntry.getKey();
            Map<Date, List<EfficiencyVO>> dateMap = cellEntry.getValue();

            for (Map.Entry<Date, List<EfficiencyVO>> dateEntry : dateMap.entrySet()) {
                Date createDate = dateEntry.getKey();
                List<EfficiencyVO> groupedList = dateEntry.getValue();
                BigDecimal totalConsuming = BigDecimal.ZERO;

                for (EfficiencyVO vo : groupedList) {
                    totalConsuming = totalConsuming.add(vo.getTimeConsuming());
                }

                BigDecimal averageConsuming = totalConsuming.divide(BigDecimal.valueOf(groupedList.size()), 2,
                        RoundingMode.HALF_UP);

                //结果集为空则添加
                if (CollectionUtils.isEmpty(vos)){
                    ProcessEfficiencyVO processVO = new ProcessEfficiencyVO();
                    processVO.setCell(cell);
                    processVO.setCreateTime(createDate);
                    if (type == 0) {
                        processVO.setBinInConsuming(averageConsuming);
                    } else if (type == 1) {
                        processVO.setIqcConsuming(averageConsuming);
                    } else if (type == 2) {
                        processVO.setCallConsuming(averageConsuming);
                    }

                    vos.add(processVO);
                }else {
                    //不为空判断是否有相同的cell和date
                    for (ProcessEfficiencyVO vo : vos) {
                        if(vo.getCell().equals(cell)&&vo.getCreateTime().equals(createDate)){
                            if (type == 0) {
                                vo.setBinInConsuming(averageConsuming);
                            } else if (type == 1) {
                                vo.setIqcConsuming(averageConsuming);
                            } else if (type == 2) {
                                vo.setCallConsuming(averageConsuming);
                            }
                        }
                    }
                    //没有相同的则添加
                    boolean exists = vos.stream()
                            .anyMatch(obj -> obj.getCell().equals(cell) && obj.getCreateTime().equals(createDate));

                    if (!exists) {
                        ProcessEfficiencyVO processVO = new ProcessEfficiencyVO();
                        processVO.setCell(cell);
                        processVO.setCreateTime(createDate);
                        if (type == 0) {
                            processVO.setBinInConsuming(averageConsuming);
                        } else if (type == 1) {
                            processVO.setIqcConsuming(averageConsuming);
                        } else if (type == 2) {
                            processVO.setCallConsuming(averageConsuming);
                        }

                        vos.add(processVO);
                    }
                }



            }
        }
        return vos;
    }

    // Helper method to truncate the time part of a Date object
    public Date truncateToDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    @Override
    public List<MissionToDo> selectReportList(MissionToDo mission) {
        List<String> codes = new ArrayList<>();
        List<MissionToDo> list = new ArrayList<>();
        //获取仓库或者cell
        if (StringUtils.isNotEmpty(mission.getCell())) {
            if (!mission.getCell().equals("All")) {
                codes.add(mission.getCell());
            } else {
                codes = reportMapper.getCellCode();
            }
            for (String s : codes) {
                MissionToDo missionToDo = new MissionToDo();
                missionToDo.setCell(s);
                list.add(missionToDo);
            }


        } else if (StringUtils.isNotEmpty(mission.getWareCode())) {
            if (!mission.getWareCode().equals("All")) {
                codes.add(mission.getWareCode());
            } else {
                codes = reportMapper.getWareCode();
            }

            for (String s : codes) {
                MissionToDo missionToDo = new MissionToDo();
                missionToDo.setWareCode(s);
                list.add(missionToDo);
            }
        }


        if (CollectionUtils.isEmpty(list)) {
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
            if (StringUtils.isNotEmpty(mission.getCell())) {
                missionToDo.setToBeReceived(receiveMap.get(missionToDo.getCell()));
                missionToDo.setToBeBin(beBinMap.get(missionToDo.getCell()));
                missionToDo.setToBeCall(beCallMap.get(missionToDo.getCell()));
                missionToDo.setToBeMove(beMoveMap.get(missionToDo.getCell()));
            } else if (StringUtils.isNotEmpty(mission.getWareCode())) {
                missionToDo.setToBeReceived(receiveMap.get(missionToDo.getWareCode()));
                missionToDo.setToBeBin(beBinMap.get(missionToDo.getWareCode()));
                missionToDo.setToBeCall(beCallMap.get(missionToDo.getWareCode()));
                missionToDo.setToBeMove(beMoveMap.get(missionToDo.getWareCode()));
            }

        }
        return list;
    }


    @Override
    public List<MissionToDo> selectProReportList(MissionToDo mission) {
        List<String> codes = new ArrayList<>();
        List<MissionToDo> list = new ArrayList<>();
        //获取仓库或者cell
        if (StringUtils.isNotEmpty(mission.getCell())) {
            if (!mission.getCell().equals("All")) {
                codes.add(mission.getCell());
            } else {
                codes = reportMapper.getCellCode();
            }
            for (String s : codes) {
                MissionToDo missionToDo = new MissionToDo();
                missionToDo.setCell(s);
                list.add(missionToDo);
            }


        } else if (StringUtils.isNotEmpty(mission.getWareCode())) {
            if (!mission.getWareCode().equals("All")) {
                codes.add(mission.getWareCode());
            } else {
                codes = reportMapper.getWareCode();
            }

            for (String s : codes) {
                MissionToDo missionToDo = new MissionToDo();
                missionToDo.setWareCode(s);
                list.add(missionToDo);
            }
        }


        if (CollectionUtils.isEmpty(list)) {
            return list;
        }
        List<MissionMap> beReceived = reportMapper.toBeReceivedPro(mission.getCell(), mission.getWareCode());
        List<MissionMap> beBin = reportMapper.toBeBinPro(mission.getCell(), mission.getWareCode());
        List<MissionMap> beMove = reportMapper.toBeMovePro(mission.getCell(), mission.getWareCode());
        //待入库map
        Map<String, Integer> receiveMap = getMap(beReceived);
        //上架map
        Map<String, Integer> beBinMap = getMap(beBin);
        //移库map
        Map<String, Integer> beMoveMap = getMap(beMove);
        //赋值list
        for (MissionToDo missionToDo : list) {
            if (StringUtils.isNotEmpty(mission.getCell())) {
                missionToDo.setToBeReceived(receiveMap.get(missionToDo.getCell()));
                missionToDo.setToBeBin(beBinMap.get(missionToDo.getCell()));
                missionToDo.setToBeMove(beMoveMap.get(missionToDo.getCell()));
            } else if (StringUtils.isNotEmpty(mission.getWareCode())) {
                missionToDo.setToBeReceived(receiveMap.get(missionToDo.getWareCode()));
                missionToDo.setToBeBin(beBinMap.get(missionToDo.getWareCode()));
                missionToDo.setToBeMove(beMoveMap.get(missionToDo.getWareCode()));
            }

        }
        return list;
    }
    //获取map
    public Map<String, Integer> getMap(List<MissionMap> list) {
        if (CollectionUtils.isEmpty(list)) {
            Map<String, Integer> map = new HashMap<>();
            return map;
        }
        List<MissionMap> filterNull = list.stream().filter(r -> r.getCode() != null).collect(Collectors.toList());
        Map<String, Integer> map = filterNull.stream().collect(Collectors.toMap(MissionMap::getCode,
                MissionMap::getNumber));
        return map;
    }

    public     Boolean genProStock(){

        return true;
    }
}
