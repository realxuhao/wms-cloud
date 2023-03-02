package com.bosch.vehiclereservation.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.vehiclereservation.api.domain.SyncDataLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository("syncDataLogMapper")
public interface SyncDataLogMapper extends BaseMapper<SyncDataLog> {


    public SyncDataLog getLastSyncData();
}
