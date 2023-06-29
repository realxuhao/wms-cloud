package com.ruoyi.common.log.service;

import com.bosch.system.api.domain.UserOperationLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author GUZ1CGD4
* @description 针对表【user_operation_log(用户操作记录表)】的数据库操作Service
* @createDate 2023-06-14 14:18:17
*/
public interface IUserOperationLogService extends IService<UserOperationLog> {

    Boolean insertUserOperationLog(String userName,String operationType,List<UserOperationLog> userOperationLog);

    Integer insertUserOperationLog(UserOperationLog userOperationLog,String userName,String operationType);

    Integer insertUserOperationLog(String ssccs,String userName,String operationType);
}
