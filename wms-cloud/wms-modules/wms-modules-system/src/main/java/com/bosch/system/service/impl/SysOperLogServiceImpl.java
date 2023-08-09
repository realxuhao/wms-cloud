package com.bosch.system.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bosch.binin.api.RemoteBinInService;
import com.bosch.binin.api.domain.vo.MaterialKanbanVO;
import com.bosch.system.domain.vo.SsccOperLogVO;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.utils.bean.BeanConverUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bosch.system.api.domain.SysOperLog;
import com.bosch.system.mapper.SysOperLogMapper;
import com.bosch.system.service.ISysOperLogService;

/**
 * 操作日志 服务层处理
 *
 * @author ruoyi
 */
@Service
public class SysOperLogServiceImpl implements ISysOperLogService {
    @Autowired
    private SysOperLogMapper operLogMapper;


    @Autowired
    private RemoteBinInService remoteBinInService;


    /**
     * 新增操作日志
     *
     * @param operLog 操作日志对象
     * @return 结果
     */
    @Override
    public int insertOperlog(SysOperLog operLog) {
        return operLogMapper.insertOperlog(operLog);
    }

    /**
     * 查询系统操作日志集合
     *
     * @param operLog 操作日志对象
     * @return 操作日志集合
     */
    @Override
    public List<SysOperLog> selectOperLogList(SysOperLog operLog) {
        return operLogMapper.selectOperLogList(operLog);
    }

    /**
     * 批量删除系统操作日志
     *
     * @param operIds 需要删除的操作日志ID
     * @return 结果
     */
    @Override
    public int deleteOperLogByIds(Long[] operIds) {
        return operLogMapper.deleteOperLogByIds(operIds);
    }

    /**
     * 查询操作日志详细
     *
     * @param operId 操作ID
     * @return 操作日志对象
     */
    @Override
    public SysOperLog selectOperLogById(Long operId) {
        return operLogMapper.selectOperLogById(operId);
    }

    /**
     * 清空操作日志
     */
    @Override
    public void cleanOperLog() {
        operLogMapper.cleanOperLog();
    }

    @Override
    public List<SysOperLog> selectSsccOperLogList(SysOperLog operLog) {
        //有的是看板的ID下发或者取消的。需要单独查询一下。
        R<List<MaterialKanbanVO>> kanbanListBySSCCR =
                remoteBinInService.getKanbanListBySSCC(operLog.getOperParam());

        if (!kanbanListBySSCCR.isSuccess()) {
            throw new ServiceException("获取捡配列表失败");
        }
        List<MaterialKanbanVO> data = kanbanListBySSCCR.getData();
        List<Long> idList = data.stream().map(MaterialKanbanVO::getId).collect(Collectors.toList());

        operLog.setKanbanIdList(idList);

        List<SysOperLog> sysOperLogs = operLogMapper.selectOperLogList(operLog);

        sysOperLogs.stream().forEach(sysOperLog->{
            SsccOperLogVO ssccOperLogVO = BeanConverUtil.conver(sysOperLog, SsccOperLogVO.class);
            String operParam = sysOperLog.getOperParam();
            JSONObject jsonObject = JSON.parseObject(operParam);
            String ssccNumber = (String)jsonObject.get("sscc");
            if (StringUtils.isEmpty(ssccNumber)){
                ssccNumber = (String)jsonObject.get("ssccNb");
            }
            if (StringUtils.isEmpty(ssccNumber)){
                ssccNumber = (String)jsonObject.get("ssccNumber");
            }

        });

        return null;
    }
}
