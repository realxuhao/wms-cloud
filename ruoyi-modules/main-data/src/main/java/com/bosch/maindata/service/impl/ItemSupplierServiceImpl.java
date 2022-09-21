package com.bosch.maindata.service.impl;

import java.util.List;
import com.ruoyi.common.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bosch.maindata.mapper.ItemSupplierMapper;
import com.bosch.maindata.domain.ItemSupplier;
import com.bosch.maindata.service.IItemSupplierService;

/**
 * 供应商物料Service业务层处理
 * 
 * @author xuhao
 * @date 2022-09-21
 */
@Service
public class ItemSupplierServiceImpl implements IItemSupplierService 
{
    @Autowired
    private ItemSupplierMapper itemSupplierMapper;

    /**
     * 查询供应商物料
     * 
     * @param id 供应商物料主键
     * @return 供应商物料
     */
    @Override
    public ItemSupplier selectItemSupplierById(Long id)
    {
        return itemSupplierMapper.selectItemSupplierById(id);
    }

    /**
     * 查询供应商物料列表
     * 
     * @param itemSupplier 供应商物料
     * @return 供应商物料
     */
    @Override
    public List<ItemSupplier> selectItemSupplierList(ItemSupplier itemSupplier)
    {
        return itemSupplierMapper.selectItemSupplierList(itemSupplier);
    }

    /**
     * 新增供应商物料
     * 
     * @param itemSupplier 供应商物料
     * @return 结果
     */
    @Override
    public int insertItemSupplier(ItemSupplier itemSupplier)
    {
        itemSupplier.setCreateTime(DateUtils.getNowDate());
        return itemSupplierMapper.insertItemSupplier(itemSupplier);
    }

    /**
     * 修改供应商物料
     * 
     * @param itemSupplier 供应商物料
     * @return 结果
     */
    @Override
    public int updateItemSupplier(ItemSupplier itemSupplier)
    {
        itemSupplier.setUpdateTime(DateUtils.getNowDate());
        return itemSupplierMapper.updateItemSupplier(itemSupplier);
    }

    /**
     * 批量删除供应商物料
     * 
     * @param ids 需要删除的供应商物料主键
     * @return 结果
     */
    @Override
    public int deleteItemSupplierByIds(Long[] ids)
    {
        return itemSupplierMapper.deleteItemSupplierByIds(ids);
    }

    /**
     * 删除供应商物料信息
     * 
     * @param id 供应商物料主键
     * @return 结果
     */
    @Override
    public int deleteItemSupplierById(Long id)
    {
        return itemSupplierMapper.deleteItemSupplierById(id);
    }
}
