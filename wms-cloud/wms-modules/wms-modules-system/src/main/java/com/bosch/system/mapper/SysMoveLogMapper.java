package com.bosch.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.system.api.domain.SysMoveLog;
import com.bosch.system.domain.SysUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户与角色关联表 数据层
 * 
 * @author ruoyi
 */

@Mapper
@Repository("sysMoveLogMapper")
public interface SysMoveLogMapper extends BaseMapper<SysMoveLog>
{

}
