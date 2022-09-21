package com.bosch.maindata.mapper;

import java.util.List;
import com.bosch.maindata.domain.ItemSupplier;

/**
 * 供应商物料Mapper接口
 * 
 * @author xuhao
 * @date 2022-09-21
 */
public interface ItemSupplierMapper 
{
    /**
     * 查询供应商物料
     * 
     * @param id 供应商物料主键
     * @return 供应商物料
     */
    public ItemSupplier selectItemSupplierById(Long id);

    /**
     * 查询供应商物料列表
     * 
     * @param itemSupplier 供应商物料
     * @return 供应商物料集合
     */
    public List<ItemSupplier> selectItemSupplierList(ItemSupplier itemSupplier);

    /**
     * 新增供应商物料
     * 
     * @param itemSupplier 供应商物料
     * @return 结果
     */
    public int insertItemSupplier(ItemSupplier itemSupplier);

    /**
     * 修改供应商物料
     * 
     * @param itemSupplier 供应商物料
     * @return 结果
     */
    public int updateItemSupplier(ItemSupplier itemSupplier);

    /**
     * 删除供应商物料
     * 
     * @param id 供应商物料主键
     * @return 结果
     */
    public int deleteItemSupplierById(Long id);

    /**
     * 批量删除供应商物料
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteItemSupplierByIds(Long[] ids);
}
