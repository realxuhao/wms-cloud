package com.bosch.masterdata.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.masterdata.api.domain.Material;
import com.bosch.masterdata.api.domain.MaterialType;
import com.bosch.masterdata.api.domain.dto.MaterialDTO;
import com.bosch.masterdata.api.domain.vo.MaterialVO;

/**
 * 物料信息Service接口
 * 
 * @author xuhao
 * @date 2022-09-22
 */
public interface IMaterialService extends IService<Material>
{
    /**
     * 查询物料信息
     * 
     * @param id 物料信息主键
     * @return 物料信息
     */
    public Material selectMaterialById(Long id);

    public MaterialVO selectMaterialVOById(Long id);
    /**
     * 查询物料信息列表
     * 
     * @param material 物料信息
     * @return 物料信息集合
     */
    public List<Material> validMaterialList(Material material);

    /**
     * 查询物料信息列表
     *
     * @param materialDTO 物料信息
     * @return 物料信息集合
     */
    public List<MaterialVO> selectMaterialVOList(MaterialDTO materialDTO);

    /**
     * 新增物料信息
     * 
     * @param material 物料信息
     * @return 结果
     */
    public int insertMaterial(Material material);
    /**
     * 新增物料
     *
     * @param materialDTO 物料信息
     * @return 结果
     */
    public int insertMaterialDTO(MaterialDTO materialDTO);
    /**
     * 修改物料信息
     * 
     * @param material 物料信息
     * @return 结果
     */
    public int updateMaterial(Material material);

    /**
     * 修改物料信息
     *
     * @param material 物料信息
     * @return 结果
     */
    public int updateMaterial(MaterialDTO material);

    /**
     * 批量删除物料信息
     * 
     * @param ids 需要删除的物料信息主键集合
     * @return 结果
     */
    public int deleteMaterialByIds(Long[] ids);

    /**
     * 删除物料信息信息
     * 
     * @param id 物料信息主键
     * @return 结果
     */
    public int deleteMaterialById(Long id);

    MaterialVO selectMaterialVOBymaterialCode(String materialCode);

    /**
     * 查询物料信息是否重复
     *
     * @param materials 物料信息
     * @return
     */
    public boolean validMaterialList(List<MaterialDTO> materials);

    /**
     * 获取typemap
     * @param codes
     * @return
     */
    public Map<String,Long> getTypeMap(List<String> codes);

    /**
     * list set value
     * @param dtos
     * @return
     */
    public List<MaterialDTO> setMaterialList(List<MaterialDTO> dtos);
}
