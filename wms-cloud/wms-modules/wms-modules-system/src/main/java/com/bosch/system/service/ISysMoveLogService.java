package com.bosch.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.system.api.domain.SysMoveLog;
import com.bosch.system.api.domain.SysUser;

import java.util.List;

/**
 * movetype 业务层
 * 
 * @author ruoyi
 */
public interface ISysMoveLogService extends IService<SysMoveLog>
{

    /**
     * 新增movetype信息
     * 
     * @param sysMoveLog  movetype信息
     * @return 结果
     */
    public int insertLog(SysMoveLog sysMoveLog);


}
