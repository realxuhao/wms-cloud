package com.bosch.maindata.service;

import java.util.List;
import com.bosch.maindata.domain.ItemLocation;

/**
 * 物料库位分配策略Service接口
 * 
 * @author xuhao
 * @date 2022-09-21
 */
public interface IItemLocationService 
{
    /**
     * 查询物料库位分配策略
     * 
     * @param id 物料库位分配策略主键
     * @return 物料库位分配策略
     */
    public ItemLocation selectItemLocationById(Long id);

    /**
     * 查询物料库位分配策略列表
     * 
     * @param itemLocation 物料库位分配策略
     * @return 物料库位分配策略集合
     */
    public List<ItemLocation> selectItemLocationList(ItemLocation itemLocation);

    /**
     * 新增物料库位分配策略
     * 
     * @param itemLocation 物料库位分配策略
     * @return 结果
     */
    public int insertItemLocation(ItemLocation itemLocation);

    /**
     * 修改物料库位分配策略
     * 
     * @param itemLocation 物料库位分配策略
     * @return 结果
     */
    public int updateItemLocation(ItemLocation itemLocation);

    /**
     * 批量删除物料库位分配策略
     * 
     * @param ids 需要删除的物料库位分配策略主键集合
     * @return 结果
     */
    public int deleteItemLocationByIds(Long[] ids);

    /**
     * 删除物料库位分配策略信息
     * 
     * @param id 物料库位分配策略主键
     * @return 结果
     */
    public int deleteItemLocationById(Long id);
}
