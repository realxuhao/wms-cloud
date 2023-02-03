package com.bosch.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.system.api.domain.SysMoveLog;
import com.bosch.system.mapper.SysMoveLogMapper;
import com.bosch.system.service.ISysMoveLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysMoveLogServiceImpl extends ServiceImpl<SysMoveLogMapper, SysMoveLog> implements ISysMoveLogService {

    @Autowired
    private SysMoveLogMapper sysMoveLogMapper;
    @Override
    public int insertLog(SysMoveLog sysMoveLog) {
        int insert = sysMoveLogMapper.insert(sysMoveLog);
        return insert;
    }
}
