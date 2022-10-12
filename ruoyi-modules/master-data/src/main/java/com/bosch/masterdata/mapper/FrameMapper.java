package com.bosch.masterdata.mapper;

import java.util.List;
import com.bosch.masterdata.domain.Frame;
import com.bosch.masterdata.domain.dto.FrameDTO;
import com.bosch.masterdata.domain.vo.FrameVO;

/**
 * 跨Mapper接口
 * 
 * @author xuhao
 * @date 2022-09-26
 */
public interface FrameMapper 
{
    /**
     * 查询跨
     * 
     * @param id 跨主键
     * @return 跨
     */
    public FrameVO selectFrameById(Long id);

    /**
     * 查询跨列表
     * 
     * @param frameDTO 跨
     * @return 跨集合
     */
    public List<FrameVO> selectFrameList(FrameDTO frameDTO);

    /**
     * 新增跨
     * 
     * @param frame 跨
     * @return 结果
     */
    public int insertFrame(Frame frame);

    /**
     * 修改跨
     * 
     * @param frame 跨
     * @return 结果
     */
    public int updateFrame(Frame frame);

    /**
     * 删除跨
     * 
     * @param id 跨主键
     * @return 结果
     */
    public int deleteFrameById(Long id);

    /**
     * 批量删除跨
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFrameByIds(Long[] ids);
}