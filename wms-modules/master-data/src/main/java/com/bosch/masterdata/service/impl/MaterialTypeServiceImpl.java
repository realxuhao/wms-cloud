package com.bosch.masterdata.service.impl;

import java.util.List;

import com.bosch.masterdata.api.domain.dto.MaterialTypeDTO;
import com.bosch.masterdata.api.domain.vo.MaterialTypeVO;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bosch.masterdata.mapper.MaterialTypeMapper;
import com.bosch.masterdata.api.domain.MaterialType;
import com.bosch.masterdata.service.IMaterialTypeService;

/**
 * 物料类型Service业务层处理
 * 
 * @author xuhao
 * @date 2022-09-22
 */
@Service
public class MaterialTypeServiceImpl implements IMaterialTypeService
{
    @Autowired
    private MaterialTypeMapper materialTypeMapper;

    /**
     * 查询物料类型
     * 
     * @param id 物料类型主键
     * @return 物料类型
     */
    @Override
    public MaterialType selectMaterialTypeById(Long id)
    {
        return materialTypeMapper.selectMaterialTypeById(id);
    }

    /**
     * 查询物料类型列表
     * 
     * @param materialType 物料类型
     * @return 物料类型
     */
    @Override
    public List<MaterialType> selectMaterialTypeList(MaterialType materialType)
    {
        return materialTypeMapper.selectMaterialTypeList(materialType);
    }

    /**
     * 新增物料类型
     * 
     * @param materialType 物料类型
     * @return 结果
     */
    @Override
    public int insertMaterialType(MaterialType materialType)
    {
        materialType.setCreateTime(DateUtils.getNowDate());
        materialType.setCreateBy(SecurityUtils.getUsername());
        return materialTypeMapper.insertMaterialType(materialType);
    }

    /**
     * 修改物料类型
     * 
     * @param materialType 物料类型
     * @return 结果
     */
    @Override
    public int updateMaterialType(MaterialType materialType)
    {
        materialType.setUpdateTime(DateUtils.getNowDate());
        materialType.setUpdateBy(SecurityUtils.getUsername());
        return materialTypeMapper.updateMaterialType(materialType);
    }

    /**
     * 批量删除物料类型
     * 
     * @param ids 需要删除的物料类型主键
     * @return 结果
     */
    @Override
    public int deleteMaterialTypeByIds(Long[] ids)
    {
        return materialTypeMapper.deleteMaterialTypeByIds(ids);
    }

    /**
     * 删除物料类型信息
     * 
     * @param id 物料类型主键
     * @return 结果
     */
    @Override
    public int deleteMaterialTypeById(Long id)
    {
        return materialTypeMapper.deleteMaterialTypeById(id);
    }

    @Override
    public List<MaterialTypeVO> selectMaterialTypeVOList(MaterialTypeDTO materialTypeDTO) {
        return materialTypeMapper.selectMaterialTypeVOList(materialTypeDTO);
    }
}
