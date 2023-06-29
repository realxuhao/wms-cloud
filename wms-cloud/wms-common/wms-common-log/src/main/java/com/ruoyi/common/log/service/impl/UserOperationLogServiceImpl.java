package com.ruoyi.common.log.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.system.api.domain.UserOperationLog;
import com.ruoyi.common.log.service.IUserOperationLogService;
import com.ruoyi.common.log.mapper.UserOperationLogMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
* @author GUZ1CGD4
* @description 针对表【user_operation_log(用户操作记录表)】的数据库操作Service实现
* @createDate 2023-06-14 14:18:17
*/
@Service
public class UserOperationLogServiceImpl extends ServiceImpl<UserOperationLogMapper, UserOperationLog>
    implements IUserOperationLogService {

    private UserOperationLogMapper userOperationLogMapper;
    @Override
    public Boolean insertUserOperationLog( Integer type,String orderNumber,String userName,Integer operationType,List<UserOperationLog> userOperationLog) {
        for (UserOperationLog operationLog : userOperationLog) {
            operationLog.setType(type);
            operationLog.setOrderNumber(orderNumber);
            operationLog.setCreateBy(userName);
            operationLog.setCreateTime(new Date());
            operationLog.setOperationType(operationType);
        }
        boolean b = this.saveBatch(userOperationLog);
        return b;
    }

    @Override
    public Boolean insertUserOperationLog(Integer type,String orderNumber,String userName,Integer operationType,UserOperationLog userOperationLog) {
        userOperationLog.setType(type);
        userOperationLog.setOrderNumber(orderNumber);
        userOperationLog.setCreateBy(userName);
        userOperationLog.setCreateTime(new Date());
        userOperationLog.setOperationType(operationType);
        boolean save = this.save(userOperationLog);
        return save;
    }


    @Override
    public Boolean insertUserOperationLog(Integer type,String orderNumber,String userName,Integer operationType,String ssccs) {
        UserOperationLog userOperationLog = new UserOperationLog();
        userOperationLog.setSsccNumber(ssccs);
        userOperationLog.setType(type);
        userOperationLog.setOrderNumber(orderNumber);
        userOperationLog.setCreateBy(userName);
        userOperationLog.setCreateTime(new Date());
        userOperationLog.setOperationType(operationType);
        boolean save = this.save(userOperationLog);
        return save;
    }
}




