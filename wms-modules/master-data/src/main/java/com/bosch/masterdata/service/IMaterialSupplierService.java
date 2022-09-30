package com.bosch.masterdata.service;

import java.util.List;
import com.bosch.masterdata.api.domain.MaterialSupplier;

/**
 * 供应商物料Service接口
 * 
 * @author xuhao
 * @date 2022-09-22
 */
public interface IMaterialSupplierService 
{
    /**
     * 查询供应商物料
     * 
     * @param id 供应商物料主键
     * @return 供应商物料
     */
    public MaterialSupplier selectMaterialSupplierById(Long id);

    /**
     * 查询供应商物料列表
     * 
     * @param materialSupplier 供应商物料
     * @return 供应商物料集合
     */
    public List<MaterialSupplier> selectMaterialSupplierList(MaterialSupplier materialSupplier);

    /**
     * 新增供应商物料
     * 
     * @param materialSupplier 供应商物料
     * @return 结果
     */
    public int insertMaterialSupplier(MaterialSupplier materialSupplier);

    /**
     * 修改供应商物料
     * 
     * @param materialSupplier 供应商物料
     * @return 结果
     */
    public int updateMaterialSupplier(MaterialSupplier materialSupplier);

    /**
     * 批量删除供应商物料
     * 
     * @param ids 需要删除的供应商物料主键集合
     * @return 结果
     */
    public int deleteMaterialSupplierByIds(Long[] ids);

    /**
     * 删除供应商物料信息
     * 
     * @param id 供应商物料主键
     * @return 结果
     */
    public int deleteMaterialSupplierById(Long id);
}
