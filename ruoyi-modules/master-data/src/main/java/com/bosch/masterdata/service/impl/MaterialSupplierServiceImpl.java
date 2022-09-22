package com.bosch.masterdata.service.impl;

import java.util.List;
import com.ruoyi.common.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bosch.masterdata.mapper.MaterialSupplierMapper;
import com.bosch.masterdata.domain.MaterialSupplier;
import com.bosch.masterdata.service.IMaterialSupplierService;

/**
 * 供应商物料Service业务层处理
 * 
 * @author xuhao
 * @date 2022-09-22
 */
@Service
public class MaterialSupplierServiceImpl implements IMaterialSupplierService 
{
    @Autowired
    private MaterialSupplierMapper materialSupplierMapper;

    /**
     * 查询供应商物料
     * 
     * @param id 供应商物料主键
     * @return 供应商物料
     */
    @Override
    public MaterialSupplier selectMaterialSupplierById(Long id)
    {
        return materialSupplierMapper.selectMaterialSupplierById(id);
    }

    /**
     * 查询供应商物料列表
     * 
     * @param materialSupplier 供应商物料
     * @return 供应商物料
     */
    @Override
    public List<MaterialSupplier> selectMaterialSupplierList(MaterialSupplier materialSupplier)
    {
        return materialSupplierMapper.selectMaterialSupplierList(materialSupplier);
    }

    /**
     * 新增供应商物料
     * 
     * @param materialSupplier 供应商物料
     * @return 结果
     */
    @Override
    public int insertMaterialSupplier(MaterialSupplier materialSupplier)
    {
        materialSupplier.setCreateTime(DateUtils.getNowDate());
        return materialSupplierMapper.insertMaterialSupplier(materialSupplier);
    }

    /**
     * 修改供应商物料
     * 
     * @param materialSupplier 供应商物料
     * @return 结果
     */
    @Override
    public int updateMaterialSupplier(MaterialSupplier materialSupplier)
    {
        materialSupplier.setUpdateTime(DateUtils.getNowDate());
        return materialSupplierMapper.updateMaterialSupplier(materialSupplier);
    }

    /**
     * 批量删除供应商物料
     * 
     * @param ids 需要删除的供应商物料主键
     * @return 结果
     */
    @Override
    public int deleteMaterialSupplierByIds(Long[] ids)
    {
        return materialSupplierMapper.deleteMaterialSupplierByIds(ids);
    }

    /**
     * 删除供应商物料信息
     * 
     * @param id 供应商物料主键
     * @return 结果
     */
    @Override
    public int deleteMaterialSupplierById(Long id)
    {
        return materialSupplierMapper.deleteMaterialSupplierById(id);
    }
}
