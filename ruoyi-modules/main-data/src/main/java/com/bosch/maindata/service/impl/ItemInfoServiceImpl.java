package com.bosch.maindata.service.impl;

import java.util.List;
import com.ruoyi.common.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bosch.maindata.mapper.ItemInfoMapper;
import com.bosch.maindata.domain.ItemInfo;
import com.bosch.maindata.service.IItemInfoService;

/**
 * 物料信息Service业务层处理
 * 
 * @author xuhao
 * @date 2022-09-21
 */
@Service
public class ItemInfoServiceImpl implements IItemInfoService 
{
    @Autowired
    private ItemInfoMapper itemInfoMapper;

    /**
     * 查询物料信息
     * 
     * @param id 物料信息主键
     * @return 物料信息
     */
    @Override
    public ItemInfo selectItemInfoById(Long id)
    {
        return itemInfoMapper.selectItemInfoById(id);
    }

    /**
     * 查询物料信息列表
     * 
     * @param itemInfo 物料信息
     * @return 物料信息
     */
    @Override
    public List<ItemInfo> selectItemInfoList(ItemInfo itemInfo)
    {
        return itemInfoMapper.selectItemInfoList(itemInfo);
    }

    /**
     * 新增物料信息
     * 
     * @param itemInfo 物料信息
     * @return 结果
     */
    @Override
    public int insertItemInfo(ItemInfo itemInfo)
    {
        itemInfo.setCreateTime(DateUtils.getNowDate());
        return itemInfoMapper.insertItemInfo(itemInfo);
    }

    /**
     * 修改物料信息
     * 
     * @param itemInfo 物料信息
     * @return 结果
     */
    @Override
    public int updateItemInfo(ItemInfo itemInfo)
    {
        itemInfo.setUpdateTime(DateUtils.getNowDate());
        return itemInfoMapper.updateItemInfo(itemInfo);
    }

    /**
     * 批量删除物料信息
     * 
     * @param ids 需要删除的物料信息主键
     * @return 结果
     */
    @Override
    public int deleteItemInfoByIds(Long[] ids)
    {
        return itemInfoMapper.deleteItemInfoByIds(ids);
    }

    /**
     * 删除物料信息信息
     * 
     * @param id 物料信息主键
     * @return 结果
     */
    @Override
    public int deleteItemInfoById(Long id)
    {
        return itemInfoMapper.deleteItemInfoById(id);
    }
}
