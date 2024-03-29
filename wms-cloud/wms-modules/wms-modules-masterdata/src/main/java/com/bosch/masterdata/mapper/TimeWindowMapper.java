package com.bosch.masterdata.mapper;

import java.util.List;
import com.bosch.masterdata.api.domain.TimeWindow;
import com.bosch.masterdata.api.domain.dto.TimeWindowDTO;
import com.bosch.masterdata.api.domain.vo.TimeWindowVO;

/**
 * 道口时间窗口Mapper接口
 * 
 * @author xuhao
 * @date 2022-09-22
 */
public interface TimeWindowMapper 
{
    /**
     * 查询道口时间窗口
     * 
     * @param id 道口时间窗口主键
     * @return 道口时间窗口
     */
    public TimeWindow selectTimeWindowById(Long id);

    /**
     * 查询道口时间窗口列表
     * 
     * @param timeWindowDTO 道口时间窗口
     * @return 道口时间窗口集合
     */
    public List<TimeWindowVO> selectTimeWindowList(TimeWindowDTO timeWindowDTO);

    /**
     * 新增道口时间窗口
     * 
     * @param timeWindow 道口时间窗口
     * @return 结果
     */
    public int insertTimeWindow(TimeWindow timeWindow);

    /**
     * 修改道口时间窗口
     * 
     * @param timeWindow 道口时间窗口
     * @return 结果
     */
    public int updateTimeWindow(TimeWindow timeWindow);

    /**
     * 删除道口时间窗口
     * 
     * @param id 道口时间窗口主键
     * @return 结果
     */
    public int deleteTimeWindowById(Long id);

    /**
     * 批量删除道口时间窗口
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTimeWindowByIds(Long[] ids);
}
