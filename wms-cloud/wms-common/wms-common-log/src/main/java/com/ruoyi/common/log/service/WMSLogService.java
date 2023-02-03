package com.ruoyi.common.log.service;

import com.bosch.system.api.RemoteLogService;
import com.bosch.system.api.domain.SysMoveLog;
import com.bosch.system.api.domain.SysOperLog;
import com.ruoyi.common.core.constant.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 异步调用日志服务
 * 
 * @author ruoyi
 */
@Service
public class WMSLogService
{
    @Autowired
    private RemoteLogService remoteLogService;

    /**
     * 保存系统日志记录
     */
    @Async
    public void saveMoveLog(SysMoveLog sysMoveLog)
    {
        remoteLogService.saveMoveLog(sysMoveLog, SecurityConstants.INNER);
    }
}
