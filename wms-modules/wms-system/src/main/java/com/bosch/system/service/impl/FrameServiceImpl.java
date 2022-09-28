package com.bosch.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bosch.system.mapper.FrameMapper;
import com.bosch.system.domain.Frame;
import com.bosch.system.service.IFrameService;

/**
 * 跨Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-09-19
 */
@Service
public class FrameServiceImpl implements IFrameService 
{
    @Autowired
    private FrameMapper frameMapper;

    /**
     * 查询跨
     * 
     * @param id 跨主键
     * @return 跨
     */
    @Override
    public Frame selectFrameById(Long id)
    {
        return frameMapper.selectFrameById(id);
    }

    /**
     * 查询跨列表
     * 
     * @param frame 跨
     * @return 跨
     */
    @Override
    public List<Frame> selectFrameList(Frame frame)
    {
        return frameMapper.selectFrameList(frame);
    }

    /**
     * 新增跨
     * 
     * @param frame 跨
     * @return 结果
     */
    @Override
    public int insertFrame(Frame frame)
    {
        return frameMapper.insertFrame(frame);
    }

    /**
     * 修改跨
     * 
     * @param frame 跨
     * @return 结果
     */
    @Override
    public int updateFrame(Frame frame)
    {
        return frameMapper.updateFrame(frame);
    }

    /**
     * 批量删除跨
     * 
     * @param ids 需要删除的跨主键
     * @return 结果
     */
    @Override
    public int deleteFrameByIds(Long[] ids)
    {
        return frameMapper.deleteFrameByIds(ids);
    }

    /**
     * 删除跨信息
     * 
     * @param id 跨主键
     * @return 结果
     */
    @Override
    public int deleteFrameById(Long id)
    {
        return frameMapper.deleteFrameById(id);
    }
}
