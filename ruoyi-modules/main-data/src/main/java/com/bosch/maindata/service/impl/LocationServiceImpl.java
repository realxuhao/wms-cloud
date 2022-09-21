package com.bosch.maindata.service.impl;

import java.util.List;
import com.ruoyi.common.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bosch.maindata.mapper.LocationMapper;
import com.bosch.maindata.domain.Location;
import com.bosch.maindata.service.ILocationService;

/**
 * 库位Service业务层处理
 * 
 * @author xuhao
 * @date 2022-09-21
 */
@Service
public class LocationServiceImpl implements ILocationService 
{
    @Autowired
    private LocationMapper locationMapper;

    /**
     * 查询库位
     * 
     * @param id 库位主键
     * @return 库位
     */
    @Override
    public Location selectLocationById(Long id)
    {
        return locationMapper.selectLocationById(id);
    }

    /**
     * 查询库位列表
     * 
     * @param location 库位
     * @return 库位
     */
    @Override
    public List<Location> selectLocationList(Location location)
    {
        return locationMapper.selectLocationList(location);
    }

    /**
     * 新增库位
     * 
     * @param location 库位
     * @return 结果
     */
    @Override
    public int insertLocation(Location location)
    {
        location.setCreateTime(DateUtils.getNowDate());
        return locationMapper.insertLocation(location);
    }

    /**
     * 修改库位
     * 
     * @param location 库位
     * @return 结果
     */
    @Override
    public int updateLocation(Location location)
    {
        location.setUpdateTime(DateUtils.getNowDate());
        return locationMapper.updateLocation(location);
    }

    /**
     * 批量删除库位
     * 
     * @param ids 需要删除的库位主键
     * @return 结果
     */
    @Override
    public int deleteLocationByIds(Long[] ids)
    {
        return locationMapper.deleteLocationByIds(ids);
    }

    /**
     * 删除库位信息
     * 
     * @param id 库位主键
     * @return 结果
     */
    @Override
    public int deleteLocationById(Long id)
    {
        return locationMapper.deleteLocationById(id);
    }
}
