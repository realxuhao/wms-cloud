package com.bosch.vehiclereservation.utils;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.security.utils.SecurityUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;


@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {

        this.setFieldValByName("uploadTime", DateUtils.getNowDate(), metaObject);
        this.setFieldValByName("uploadUser", SecurityUtils.getLoginUser().getUsername(), metaObject);
        this.setFieldValByName("createTime", DateUtils.getNowDate(), metaObject);
        this.setFieldValByName("createBy", SecurityUtils.getLoginUser().getUsername(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", DateUtils.getNowDate(), metaObject);
        this.setFieldValByName("updateUser", SecurityUtils.getLoginUser().getUsername(), metaObject);
    }
}


