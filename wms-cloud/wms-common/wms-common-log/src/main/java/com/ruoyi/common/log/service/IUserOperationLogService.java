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
    //        UserOperationLog userOperationLog=new UserOperationLog();
//        userOperationLog.setType(0);
//        userOperationLog.setOrderNumber("001order");
//        userOperationLog.setCode("001code");
//        userOperationLog.setSsccNumber("001");
//        userOperationLog.setOperationType(UserOperationType.Import.getCode());
//
//        List<UserOperationLog> list=new ArrayList<>();
//        list.add(userOperationLog);
//        userOperationLog.setSsccNumber("002");
//        list.add(userOperationLog);
//
//        Boolean aBoolean = iUserOperationLogService.insertUserOperationLog(0, "001order", SecurityUtils.getUsername(), UserOperationType.Import.getCode(), list);


    /**
     *
     * @param type 0：物料；1：成品 MaterialType枚举
     * @param orderNumber 订单号
     * @param userName 用户名
     * @param operationType 操作类型 UserOperationType枚举
     * @param userOperationLog sscc码、物料号集合
     * @return
     */
    Boolean insertUserOperationLog(Integer type,String orderNumber,String userName,Integer operationType,List<UserOperationLog> userOperationLog);

    /**
     *
     * @param userOperationLog sscc码、物料号
     * @param userName 用户名
     * @param operationType 操作类型 UserOperationType枚举
     * @param type 0：物料；1：成品 MaterialType枚举
     * @param orderNumber 订单号
     * @return
     */
    Boolean insertUserOperationLog(Integer type,String orderNumber,String userName,Integer operationType,UserOperationLog userOperationLog);

    /**
     *
     * @param ssccs sscc码
     * @param userName 用户名
     * @param operationType 操作类型 UserOperationType枚举
     * @param type 0：物料；1：成品 MaterialType枚举
     * @param orderNumber 订单号
     * @return
     */
    Boolean insertUserOperationLog(Integer type,String orderNumber,String userName,Integer operationType,String ssccs,String code);
}
