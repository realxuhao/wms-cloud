package com.bosch.maindata.service;

import java.util.List;
import com.bosch.maindata.domain.ItemInfo;

/**
 * 物料信息Service接口
 * 
 * @author xuhao
 * @date 2022-09-21
 */
public interface IItemInfoService 
{
    /**
     * 查询物料信息
     * 
     * @param id 物料信息主键
     * @return 物料信息
     */
    public ItemInfo selectItemInfoById(Long id);

    /**
     * 查询物料信息列表
     * 
     * @param itemInfo 物料信息
     * @return 物料信息集合
     */
    public List<ItemInfo> selectItemInfoList(ItemInfo itemInfo);

    /**
     * 新增物料信息
     * 
     * @param itemInfo 物料信息
     * @return 结果
     */
    public int insertItemInfo(ItemInfo itemInfo);

    /**
     * 修改物料信息
     * 
     * @param itemInfo 物料信息
     * @return 结果
     */
    public int updateItemInfo(ItemInfo itemInfo);

    /**
     * 批量删除物料信息
     * 
     * @param ids 需要删除的物料信息主键集合
     * @return 结果
     */
    public int deleteItemInfoByIds(Long[] ids);

    /**
     * 删除物料信息信息
     * 
     * @param id 物料信息主键
     * @return 结果
     */
    public int deleteItemInfoById(Long id);
}
