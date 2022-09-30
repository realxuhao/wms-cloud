package com.bosch.masterdata.service.impl;

import java.util.List;

import com.bosch.masterdata.api.domain.dto.MaterialDTO;
import com.bosch.masterdata.api.domain.vo.MaterialVO;
import com.bosch.masterdata.utils.BeanConverUtil;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bosch.masterdata.mapper.MaterialMapper;
import com.bosch.masterdata.api.domain.Material;
import com.bosch.masterdata.service.IMaterialService;

/**
 * 物料信息Service业务层处理
 * 
 * @author xuhao
 * @date 2022-09-22
 */
@Service
public class MaterialServiceImpl implements IMaterialService 
{
    @Autowired
    private MaterialMapper materialMapper;

    /**
     * 查询物料信息
     * 
     * @param id 物料信息主键
     * @return 物料信息
     */
    @Override
    public Material selectMaterialById(Long id)
    {
        return materialMapper.selectMaterialById(id);
    }

    @Override
    public MaterialVO selectMaterialVOById(Long id) {
        return materialMapper.selectMaterialVOById(id);
    }

    /**
     * 查询物料信息列表
     * 
     * @param material 物料信息
     * @return 物料信息
     */
    @Override
    public List<Material> selectMaterialList(Material material)
    {
        return materialMapper.selectMaterialList(material);
    }

    @Override
    public List<MaterialVO> selectMaterialVOList(MaterialDTO materialDTO) {
        return materialMapper.selectMaterialVOList(materialDTO);
    }

    /**
     * 新增物料信息
     * 
     * @param material 物料信息
     * @return 结果
     */
    @Override
    public int insertMaterial(Material material)
    {
        material.setCreateTime(DateUtils.getNowDate());
        return materialMapper.insertMaterial(material);
    }

    /**
     * 新增物料DTO
     *
     * @param materialDTO 物料信息
     * @return 结果
     */
    @Override
    public int insertMaterialDTO(MaterialDTO materialDTO) {
        Material material = BeanConverUtil.conver(materialDTO, Material.class);
        material.setCreateTime(DateUtils.getNowDate());
        material.setCreateBy(SecurityUtils.getUsername());
        return materialMapper.insertMaterial(material);
    }

    /**
     * 修改物料信息
     * 
     * @param material 物料信息
     * @return 结果
     */
    @Override
    public int updateMaterial(Material material)
    {
        material.setUpdateTime(DateUtils.getNowDate());
        return materialMapper.updateMaterial(material);
    }

    @Override
    public int updateMaterial(MaterialDTO materialDTO) {
        Material material = BeanConverUtil.conver(materialDTO, Material.class);
        material.setUpdateTime(DateUtils.getNowDate());
        material.setCreateBy(SecurityUtils.getUsername());
        return materialMapper.updateMaterial(material);
    }

    /**
     * 批量删除物料信息
     * 
     * @param ids 需要删除的物料信息主键
     * @return 结果
     */
    @Override
    public int deleteMaterialByIds(Long[] ids)
    {
        return materialMapper.deleteMaterialByIds(ids);
    }

    /**
     * 删除物料信息信息
     * 
     * @param id 物料信息主键
     * @return 结果
     */
    @Override
    public int deleteMaterialById(Long id)
    {
        return materialMapper.deleteMaterialById(id);
    }

    @Override
    public MaterialVO selectMaterialVOBymaterialCode(String materialCode) {
        return materialMapper.selectMaterialVOByMaterialCode(materialCode);
    }
}
