package com.bosch.masterdata.mapper;

import java.util.List;
import com.bosch.masterdata.api.domain.Material;
import com.bosch.masterdata.api.domain.dto.MaterialDTO;
import com.bosch.masterdata.api.domain.vo.MaterialVO;

/**
 * 物料信息Mapper接口
 * 
 * @author xuhao
 * @date 2022-09-22
 */
public interface MaterialMapper 
{
    /**
     * 查询物料信息
     * 
     * @param id 物料信息主键
     * @return 物料信息
     */
    public Material selectMaterialById(Long id);

    /**
     * 查询物料信息列表
     * 
     * @param material 物料信息
     * @return 物料信息集合
     */
    public List<Material> selectMaterialList(Material material);

    /**
     * 新增物料信息
     * 
     * @param material 物料信息
     * @return 结果
     */
    public int insertMaterial(Material material);

    /**
     * 修改物料信息
     * 
     * @param material 物料信息
     * @return 结果
     */
    public int updateMaterial(Material material);

    /**
     * 删除物料信息
     * 
     * @param id 物料信息主键
     * @return 结果
     */
    public int deleteMaterialById(Long id);

    /**
     * 批量删除物料信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMaterialByIds(Long[] ids);

    /**
     * 查询物料信息列表
     *
     * @param materialDTO 物料信息
     * @return 物料信息集合
     */
    public List<MaterialVO> selectMaterialVOList(MaterialDTO materialDTO);

    /**
     * 查询物料信息
     *
     * @param id 物料信息主键
     * @return 物料信息
     */
    public MaterialVO selectMaterialVOById(Long id);

    MaterialVO selectMaterialVOByMaterialCode(String materialCode);
}
