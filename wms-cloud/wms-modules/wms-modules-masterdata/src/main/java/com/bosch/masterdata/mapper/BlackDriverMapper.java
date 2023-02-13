package com.bosch.masterdata.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.masterdata.api.domain.BlackDriver;
import com.bosch.masterdata.api.domain.Ware;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 司机黑名单Mapper接口
 *
 * @author taojd
 * @date 2023-02-13
 */
@Mapper
@Repository("blackDriverMapper")
public interface BlackDriverMapper extends BaseMapper<BlackDriver> {

    /**
     * 查询司机列表
     *
     * @param blackDriver 司机
     * @return 司机集合
     */
    public List<BlackDriver> selectBlackDriverList(BlackDriver blackDriver);


}
