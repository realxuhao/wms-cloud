package com.bosch.masterdata.service.impl;

import java.util.List;

import com.bosch.masterdata.api.domain.dto.FrameDTO;
import com.bosch.masterdata.api.domain.vo.FrameVO;
import com.bosch.masterdata.utils.BeanConverUtil;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bosch.masterdata.mapper.FrameMapper;
import com.bosch.masterdata.api.domain.Frame;
import com.bosch.masterdata.service.IFrameService;

/**
 * 跨Service业务层处理
 *
 * @author xuhao
 * @date 2022-09-26
 */
@Service
public class FrameServiceImpl implements IFrameService {
    @Autowired
    private FrameMapper frameMapper;

    /**
     * 查询跨
     *
     * @param id 跨主键
     * @return 跨
     */
    @Override
    public FrameVO selectFrameById(Long id) {
        return frameMapper.selectFrameById(id);
    }

    /**
     * 查询跨列表
     *
     * @param frameDTO 跨
     * @return 跨
     */
    @Override
    public List<FrameVO> selectFrameList(FrameDTO frameDTO) {
        return frameMapper.selectFrameList(frameDTO);
    }

    /**
     * 新增跨
     *
     * @param frameDTO 跨
     * @return 结果
     */
    @Override
    public int insertFrame(FrameDTO frameDTO) {
        Frame frame = BeanConverUtil.conver(frameDTO, Frame.class);
        frame.setCreateTime(DateUtils.getNowDate());
        frame.setCreateBy(SecurityUtils.getUsername());
        return frameMapper.insertFrame(frame);
    }

    /**
     * 修改跨
     *
     * @param frame 跨
     * @return 结果
     */
    @Override
    public int updateFrame(FrameDTO frameDTO) {
        Frame frame = BeanConverUtil.conver(frameDTO, Frame.class);
        frame.setUpdateTime(DateUtils.getNowDate());
        frame.setUpdateBy(SecurityUtils.getUsername());
        return frameMapper.updateFrame(frame);
    }

    /**
     * 批量删除跨
     *
     * @param ids 需要删除的跨主键
     * @return 结果
     */
    @Override
    public int deleteFrameByIds(Long[] ids) {
        return frameMapper.deleteFrameByIds(ids);
    }

    /**
     * 删除跨信息
     *
     * @param id 跨主键
     * @return 结果
     */
    @Override
    public int deleteFrameById(Long id) {
        return frameMapper.deleteFrameById(id);
    }
}
