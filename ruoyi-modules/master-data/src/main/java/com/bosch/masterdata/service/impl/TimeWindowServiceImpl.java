package com.bosch.masterdata.service.impl;

import java.util.List;
import com.ruoyi.common.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bosch.masterdata.mapper.TimeWindowMapper;
import com.bosch.masterdata.domain.TimeWindow;
import com.bosch.masterdata.service.ITimeWindowService;

/**
 * 道口时间窗口Service业务层处理
 * 
 * @author xuhao
 * @date 2022-09-22
 */
@Service
public class TimeWindowServiceImpl implements ITimeWindowService 
{
    @Autowired
    private TimeWindowMapper timeWindowMapper;

    /**
     * 查询道口时间窗口
     * 
     * @param id 道口时间窗口主键
     * @return 道口时间窗口
     */
    @Override
    public TimeWindow selectTimeWindowById(Long id)
    {
        return timeWindowMapper.selectTimeWindowById(id);
    }

    /**
     * 查询道口时间窗口列表
     * 
     * @param timeWindow 道口时间窗口
     * @return 道口时间窗口
     */
    @Override
    public List<TimeWindow> selectTimeWindowList(TimeWindow timeWindow)
    {
        return timeWindowMapper.selectTimeWindowList(timeWindow);
    }

    /**
     * 新增道口时间窗口
     * 
     * @param timeWindow 道口时间窗口
     * @return 结果
     */
    @Override
    public int insertTimeWindow(TimeWindow timeWindow)
    {
        timeWindow.setCreateTime(DateUtils.getNowDate());
        return timeWindowMapper.insertTimeWindow(timeWindow);
    }

    /**
     * 修改道口时间窗口
     * 
     * @param timeWindow 道口时间窗口
     * @return 结果
     */
    @Override
    public int updateTimeWindow(TimeWindow timeWindow)
    {
        timeWindow.setUpdateTime(DateUtils.getNowDate());
        return timeWindowMapper.updateTimeWindow(timeWindow);
    }

    /**
     * 批量删除道口时间窗口
     * 
     * @param ids 需要删除的道口时间窗口主键
     * @return 结果
     */
    @Override
    public int deleteTimeWindowByIds(Long[] ids)
    {
        return timeWindowMapper.deleteTimeWindowByIds(ids);
    }

    /**
     * 删除道口时间窗口信息
     * 
     * @param id 道口时间窗口主键
     * @return 结果
     */
    @Override
    public int deleteTimeWindowById(Long id)
    {
        return timeWindowMapper.deleteTimeWindowById(id);
    }
}
