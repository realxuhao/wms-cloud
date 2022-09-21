package com.bosch.maindata.service.impl;

import java.util.List;
import com.ruoyi.common.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bosch.maindata.mapper.ItemTypeMapper;
import com.bosch.maindata.domain.ItemType;
import com.bosch.maindata.service.IItemTypeService;

/**
 * 物料类型Service业务层处理
 * 
 * @author xuhao
 * @date 2022-09-21
 */
@Service
public class ItemTypeServiceImpl implements IItemTypeService 
{
    @Autowired
    private ItemTypeMapper itemTypeMapper;

    /**
     * 查询物料类型
     * 
     * @param id 物料类型主键
     * @return 物料类型
     */
    @Override
    public ItemType selectItemTypeById(Long id)
    {
        return itemTypeMapper.selectItemTypeById(id);
    }

    /**
     * 查询物料类型列表
     * 
     * @param itemType 物料类型
     * @return 物料类型
     */
    @Override
    public List<ItemType> selectItemTypeList(ItemType itemType)
    {
        return itemTypeMapper.selectItemTypeList(itemType);
    }

    /**
     * 新增物料类型
     * 
     * @param itemType 物料类型
     * @return 结果
     */
    @Override
    public int insertItemType(ItemType itemType)
    {
        itemType.setCreateTime(DateUtils.getNowDate());
        return itemTypeMapper.insertItemType(itemType);
    }

    /**
     * 修改物料类型
     * 
     * @param itemType 物料类型
     * @return 结果
     */
    @Override
    public int updateItemType(ItemType itemType)
    {
        itemType.setUpdateTime(DateUtils.getNowDate());
        return itemTypeMapper.updateItemType(itemType);
    }

    /**
     * 批量删除物料类型
     * 
     * @param ids 需要删除的物料类型主键
     * @return 结果
     */
    @Override
    public int deleteItemTypeByIds(Long[] ids)
    {
        return itemTypeMapper.deleteItemTypeByIds(ids);
    }

    /**
     * 删除物料类型信息
     * 
     * @param id 物料类型主键
     * @return 结果
     */
    @Override
    public int deleteItemTypeById(Long id)
    {
        return itemTypeMapper.deleteItemTypeById(id);
    }
}
