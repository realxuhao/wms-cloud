package com.bosch.masterdata.service.impl;

import java.util.List;

import com.bosch.masterdata.api.domain.dto.TimeWindowDTO;
import com.bosch.masterdata.api.domain.vo.TimeWindowVO;
import com.bosch.masterdata.utils.BeanConverUtil;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bosch.masterdata.mapper.TimeWindowMapper;
import com.bosch.masterdata.api.domain.TimeWindow;
import com.bosch.masterdata.service.ITimeWindowService;

/**
 * 道口时间窗口Service业务层处理
 *
 * @author xuhao
 * @date 2022-09-22
 */
@Service
public class TimeWindowServiceImpl implements ITimeWindowService {
    @Autowired
    private TimeWindowMapper timeWindowMapper;

    /**
     * 查询道口时间窗口
     *
     * @param id 道口时间窗口主键
     * @return 道口时间窗口
     */
    @Override
    public TimeWindow selectTimeWindowById(Long id) {
        return timeWindowMapper.selectTimeWindowById(id);
    }

    @Override
    public List<TimeWindow> selectTimeWindowByWareId(Long wareId){
        return timeWindowMapper.selectTimeWindowByWareId(wareId);
    }

    /**
     * 查询道口时间窗口列表
     *
     * @param timeWindowDTO 道口时间窗口
     * @return 道口时间窗口
     */
    @Override
    public List<TimeWindowVO> selectTimeWindowList(TimeWindowDTO timeWindowDTO) {
        return timeWindowMapper.selectTimeWindowList(timeWindowDTO);
    }

    /**
     * 新增道口时间窗口
     *
     * @param timeWindowDTO 道口时间窗口
     * @return 结果
     */
    @Override
    public int insertTimeWindow(TimeWindowDTO timeWindowDTO) {
        TimeWindow timeWindow = BeanConverUtil.conver(timeWindowDTO, TimeWindow.class);
        timeWindow.setCreateTime(DateUtils.getNowDate());
        timeWindow.setCreateBy(SecurityUtils.getUsername());
        return timeWindowMapper.insertTimeWindow(timeWindow);
    }

    /**
     * 修改道口时间窗口
     *
     * @param timeWindowDTO 道口时间窗口
     * @return 结果
     */
    @Override
    public int updateTimeWindow(TimeWindowDTO timeWindowDTO) {
        TimeWindow timeWindow = BeanConverUtil.conver(timeWindowDTO, TimeWindow.class);
        timeWindow.setUpdateTime(DateUtils.getNowDate());
        return timeWindowMapper.updateTimeWindow(timeWindow);
    }

    @Override
    public int saveTimeWindow(List<TimeWindowDTO> timeWindowList) {
        int i = 0;
        for (TimeWindowDTO timeWindowDTO : timeWindowList) {
            if (timeWindowDTO.getId() == null) {
                i += this.insertTimeWindow(timeWindowDTO);
            } else {
                this.updateTimeWindow(timeWindowDTO);
            }
        }
        return i;
    }

    /**
     * 批量删除道口时间窗口
     *
     * @param ids 需要删除的道口时间窗口主键
     * @return 结果
     */
    @Override
    public int deleteTimeWindowByIds(Long[] ids) {
        return timeWindowMapper.deleteTimeWindowByIds(ids);
    }

    /**
     * 删除道口时间窗口信息
     *
     * @param id 道口时间窗口主键
     * @return 结果
     */
    @Override
    public int deleteTimeWindowById(Long id) {
        return timeWindowMapper.deleteTimeWindowById(id);
    }
}
