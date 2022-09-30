package com.bosch.masterdata.service.impl;

import java.util.List;

import com.bosch.masterdata.api.domain.dto.AreaDTO;
import com.bosch.masterdata.api.domain.vo.AreaVO;
import com.bosch.masterdata.utils.BeanConverUtil;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bosch.masterdata.mapper.AreaMapper;
import com.bosch.masterdata.api.domain.Area;
import com.bosch.masterdata.service.IAreaService;

/**
 * 区域Service业务层处理
 * 
 * @author xuhao
 * @date 2022-09-26
 */
@Service
public class AreaServiceImpl implements IAreaService 
{
    @Autowired
    private AreaMapper areaMapper;

    /**
     * 查询区域
     * 
     * @param id 区域主键
     * @return 区域
     */
    @Override
    public Area selectAreaById(Integer id)
    {
        return areaMapper.selectAreaById(id);
    }

    /**
     * 查询区域列表
     * 
     * @param area 区域
     * @return 区域
     */
    @Override
    public List<Area> selectAreaList(Area area)
    {
        return areaMapper.selectAreaList(area);
    }

    @Override
    public List<AreaVO> selectAreaVOList(AreaDTO areaDTO) {
        Area area = BeanConverUtil.conver(areaDTO, Area.class);

        return BeanConverUtil.converList(areaMapper.selectAreaVOList(area),AreaVO.class);
    }

    /**
     * 新增区域
     * 
     * @param area 区域
     * @return 结果
     */
    @Override
    public int insertArea(Area area)
    {
        area.setCreateTime(DateUtils.getNowDate());
        return areaMapper.insertArea(area);
    }

    @Override
    public int insertArea(AreaDTO areaDTO) {
        Area area = BeanConverUtil.conver(areaDTO, Area.class);
        area.setCreateTime(DateUtils.getNowDate());
        area.setCreateBy(SecurityUtils.getUsername());
        return areaMapper.insertArea(area);
    }

    /**
     * 修改区域
     * 
     * @param area 区域
     * @return 结果
     */
    @Override
    public int updateArea(Area area)
    {
        area.setUpdateTime(DateUtils.getNowDate());
        return areaMapper.updateArea(area);
    }

    @Override
    public int updateArea(AreaDTO areaDTO) {
        Area area = BeanConverUtil.conver(areaDTO, Area.class);
        area.setUpdateBy(SecurityUtils.getUsername());
        area.setUpdateTime(DateUtils.getNowDate());
        return areaMapper.updateArea(area);
    }

    /**
     * 批量删除区域
     * 
     * @param ids 需要删除的区域主键
     * @return 结果
     */
    @Override
    public int deleteAreaByIds(Integer[] ids)
    {
        return areaMapper.deleteAreaByIds(ids);
    }

    /**
     * 删除区域信息
     * 
     * @param id 区域主键
     * @return 结果
     */
    @Override
    public int deleteAreaById(Integer id)
    {
        return areaMapper.deleteAreaById(id);
    }
}
