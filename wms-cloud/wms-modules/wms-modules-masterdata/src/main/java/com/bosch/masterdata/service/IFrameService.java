package com.bosch.masterdata.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.masterdata.api.domain.Area;
import com.bosch.masterdata.api.domain.Frame;
import com.bosch.masterdata.api.domain.dto.AreaDTO;
import com.bosch.masterdata.api.domain.dto.FrameDTO;
import com.bosch.masterdata.api.domain.vo.FrameVO;

/**
 * 跨Service接口
 * 
 * @author xuhao
 * @date 2022-09-26
 */
public interface IFrameService extends IService<Frame>
{
    /**
     * 查询跨
     * 
     * @param id 跨主键
     * @return 跨
     */
    public FrameVO selectFrameById(Long id);


    /**
     * 查询跨
     *
     * @param code
     * @return 跨
     */
    public FrameVO selectFrameByCode(String code);


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
     * @param frameDTO 跨
     * @return 结果
     */
    public int insertFrame(FrameDTO frameDTO);

    public int saveOrUp(List<Frame> dos);
    /**
     * 修改跨
     * 
     * @param frameDTO 跨
     * @return 结果
     */
    public int updateFrame(FrameDTO frameDTO);

    /**
     * 批量删除跨
     * 
     * @param ids 需要删除的跨主键集合
     * @return 结果
     */
    public int deleteFrameByIds(Long[] ids);

    /**
     * 删除跨信息
     * 
     * @param id 跨主键
     * @return 结果
     */
    public int deleteFrameById(Long id);

    public Map<String,Long> getTypeMap(List<String> codes);

    /**
     * 重复校验
     * @param codes
     * @return
     */
    public boolean validList(List<String> codes);

    /**
     * 赋值
     * @param dtos
     * @return
     */
    public List<FrameDTO> setValue(List<FrameDTO> dtos);
}
