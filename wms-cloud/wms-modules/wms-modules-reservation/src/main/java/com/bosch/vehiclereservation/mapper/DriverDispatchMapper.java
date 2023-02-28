package com.bosch.vehiclereservation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.vehiclereservation.api.domain.DriverDispatch;
import com.bosch.vehiclereservation.api.domain.vo.DriverDispatchVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 车辆调度Mapper接口
 *
 * @author taojd
 */
@Mapper
@Repository("driverDispatchMapper")
public interface DriverDispatchMapper extends BaseMapper<DriverDispatch> {

    /**
     * 查询当天签到车辆信息列表
     *
     * @param wareId 仓库id
     * @return 车辆调度信息列表
     */
    public List<DriverDispatchVO> selectTodaySignData(@Param("wareId") Long wareId, @Param("statusList") List<Integer> statusList);


    /**
     * 查询当天未签到车辆信息列表
     *
     * @return
     */
    public List<DriverDispatchVO> selectTodayNotSignData();

    /**
     * 获取最大排序值
     *
     * @return
     */
    public Integer getMaxSortNo();

    public Integer updateSortNo(@Param("dispatchId") Long dispatchId, @Param("startIndex") Integer startIndex, @Param("endIndex") Integer endIndex, @Param("opt") String opt);
}
