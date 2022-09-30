package com.bosch.masterdata.service;

import java.util.List;
import com.bosch.masterdata.api.domain.MaterialType;
import com.bosch.masterdata.api.domain.dto.MaterialTypeDTO;
import com.bosch.masterdata.api.domain.vo.MaterialTypeVO;

/**
 * 物料类型Service接口
 * 
 * @author xuhao
 * @date 2022-09-22
 */
public interface IMaterialTypeService 
{
    /**
     * 查询物料类型
     * 
     * @param id 物料类型主键
     * @return 物料类型
     */
    public MaterialType selectMaterialTypeById(Long id);

    /**
     * 查询物料类型列表
     * 
     * @param materialType 物料类型
     * @return 物料类型集合
     */
    public List<MaterialType> selectMaterialTypeList(MaterialType materialType);

    /**
     * 新增物料类型
     * 
     * @param materialType 物料类型
     * @return 结果
     */
    public int insertMaterialType(MaterialType materialType);

    /**
     * 修改物料类型
     * 
     * @param materialType 物料类型
     * @return 结果
     */
    public int updateMaterialType(MaterialType materialType);

    /**
     * 批量删除物料类型
     * 
     * @param ids 需要删除的物料类型主键集合
     * @return 结果
     */
    public int deleteMaterialTypeByIds(Long[] ids);

    /**
     * 删除物料类型信息
     * 
     * @param id 物料类型主键
     * @return 结果
     */
    public int deleteMaterialTypeById(Long id);

    List<MaterialTypeVO> selectMaterialTypeVOList(MaterialTypeDTO materialTypeDTO);
}
