package com.bosch.masterdata.service;

import java.util.List;
import com.bosch.masterdata.domain.MaterialBin;
import com.bosch.masterdata.domain.dto.MaterialBinDTO;
import com.bosch.masterdata.domain.vo.MaterialBinVO;

/**
 * 物料库位分配策略Service接口
 * 
 * @author xuhao
 * @date 2022-09-22
 */
public interface IMaterialBinService 
{
    /**
     * 查询物料库位分配策略
     * 
     * @param id 物料库位分配策略主键
     * @return 物料库位分配策略
     */
    public MaterialBinVO selectMaterialBinById(Long id);

    /**
     * 查询物料库位分配策略列表
     * 
     * @param materialBinDTO 物料库位分配策略
     * @return 物料库位分配策略集合
     */
    public List<MaterialBinVO> selectMaterialBinList(MaterialBinDTO materialBinDTO);

    /**
     * 新增物料库位分配策略
     * 
     * @param materialBin 物料库位分配策略
     * @return 结果
     */
    public int insertMaterialBin(MaterialBin materialBin);

    /**
     * 修改物料库位分配策略
     * 
     * @param materialBin 物料库位分配策略
     * @return 结果
     */
    public int updateMaterialBin(MaterialBin materialBin);

    /**
     * 批量删除物料库位分配策略
     * 
     * @param ids 需要删除的物料库位分配策略主键集合
     * @return 结果
     */
    public int deleteMaterialBinByIds(Long[] ids);

    /**
     * 删除物料库位分配策略信息
     * 
     * @param id 物料库位分配策略主键
     * @return 结果
     */
    public int deleteMaterialBinById(Long id);
}
