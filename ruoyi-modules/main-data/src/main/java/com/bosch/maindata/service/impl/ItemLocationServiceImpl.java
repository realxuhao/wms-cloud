package com.bosch.maindata.service.impl;

import java.util.List;
import com.ruoyi.common.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bosch.maindata.mapper.ItemLocationMapper;
import com.bosch.maindata.domain.ItemLocation;
import com.bosch.maindata.service.IItemLocationService;

/**
 * 物料库位分配策略Service业务层处理
 * 
 * @author xuhao
 * @date 2022-09-21
 */
@Service
public class ItemLocationServiceImpl implements IItemLocationService 
{
    @Autowired
    private ItemLocationMapper itemLocationMapper;

    /**
     * 查询物料库位分配策略
     * 
     * @param id 物料库位分配策略主键
     * @return 物料库位分配策略
     */
    @Override
    public ItemLocation selectItemLocationById(Long id)
    {
        return itemLocationMapper.selectItemLocationById(id);
    }

    /**
     * 查询物料库位分配策略列表
     * 
     * @param itemLocation 物料库位分配策略
     * @return 物料库位分配策略
     */
    @Override
    public List<ItemLocation> selectItemLocationList(ItemLocation itemLocation)
    {
        return itemLocationMapper.selectItemLocationList(itemLocation);
    }

    /**
     * 新增物料库位分配策略
     * 
     * @param itemLocation 物料库位分配策略
     * @return 结果
     */
    @Override
    public int insertItemLocation(ItemLocation itemLocation)
    {
        itemLocation.setCreateTime(DateUtils.getNowDate());
        return itemLocationMapper.insertItemLocation(itemLocation);
    }

    /**
     * 修改物料库位分配策略
     * 
     * @param itemLocation 物料库位分配策略
     * @return 结果
     */
    @Override
    public int updateItemLocation(ItemLocation itemLocation)
    {
        itemLocation.setUpdateTime(DateUtils.getNowDate());
        return itemLocationMapper.updateItemLocation(itemLocation);
    }

    /**
     * 批量删除物料库位分配策略
     * 
     * @param ids 需要删除的物料库位分配策略主键
     * @return 结果
     */
    @Override
    public int deleteItemLocationByIds(Long[] ids)
    {
        return itemLocationMapper.deleteItemLocationByIds(ids);
    }

    /**
     * 删除物料库位分配策略信息
     * 
     * @param id 物料库位分配策略主键
     * @return 结果
     */
    @Override
    public int deleteItemLocationById(Long id)
    {
        return itemLocationMapper.deleteItemLocationById(id);
    }
}
