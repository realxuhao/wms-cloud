package com.bosch.maindata.mapper;

import java.util.List;
import com.bosch.maindata.domain.ItemType;

/**
 * 物料类型Mapper接口
 * 
 * @author xuhao
 * @date 2022-09-21
 */
public interface ItemTypeMapper 
{
    /**
     * 查询物料类型
     * 
     * @param id 物料类型主键
     * @return 物料类型
     */
    public ItemType selectItemTypeById(Long id);

    /**
     * 查询物料类型列表
     * 
     * @param itemType 物料类型
     * @return 物料类型集合
     */
    public List<ItemType> selectItemTypeList(ItemType itemType);

    /**
     * 新增物料类型
     * 
     * @param itemType 物料类型
     * @return 结果
     */
    public int insertItemType(ItemType itemType);

    /**
     * 修改物料类型
     * 
     * @param itemType 物料类型
     * @return 结果
     */
    public int updateItemType(ItemType itemType);

    /**
     * 删除物料类型
     * 
     * @param id 物料类型主键
     * @return 结果
     */
    public int deleteItemTypeById(Long id);

    /**
     * 批量删除物料类型
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteItemTypeByIds(Long[] ids);
}
