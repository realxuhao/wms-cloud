package com.ruoyi.common.log.service;

import com.ruoyi.common.log.domain.UserOperationLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author GUZ1CGD4
* @description 针对表【user_operation_log(用户操作记录表)】的数据库操作Service
* @createDate 2023-06-14 14:18:17
*/
public interface IUserOperationLogService extends IService<UserOperationLog> {

    Integer insertUserOperationLog(List<UserOperationLog> userOperationLog,String userName);

    Integer insertUserOperationLog(UserOperationLog userOperationLog,String userName);

    Integer insertUserOperationLog(List<String> ssccs,String userName,String operationType);

    Integer insertUserOperationLog(String ssccs,String userName,String operationType);
}
