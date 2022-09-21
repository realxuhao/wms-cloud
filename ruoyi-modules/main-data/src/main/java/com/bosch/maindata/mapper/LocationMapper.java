package com.bosch.maindata.mapper;

import java.util.List;
import com.bosch.maindata.domain.Location;

/**
 * 库位Mapper接口
 * 
 * @author xuhao
 * @date 2022-09-21
 */
public interface LocationMapper 
{
    /**
     * 查询库位
     * 
     * @param id 库位主键
     * @return 库位
     */
    public Location selectLocationById(Long id);

    /**
     * 查询库位列表
     * 
     * @param location 库位
     * @return 库位集合
     */
    public List<Location> selectLocationList(Location location);

    /**
     * 新增库位
     * 
     * @param location 库位
     * @return 结果
     */
    public int insertLocation(Location location);

    /**
     * 修改库位
     * 
     * @param location 库位
     * @return 结果
     */
    public int updateLocation(Location location);

    /**
     * 删除库位
     * 
     * @param id 库位主键
     * @return 结果
     */
    public int deleteLocationById(Long id);

    /**
     * 批量删除库位
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteLocationByIds(Long[] ids);
}
