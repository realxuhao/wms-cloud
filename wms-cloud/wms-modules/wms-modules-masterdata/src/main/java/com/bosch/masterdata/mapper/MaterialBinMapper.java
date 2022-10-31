package com.bosch.masterdata.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.masterdata.api.domain.Material;
import com.bosch.masterdata.api.domain.MaterialBin;
import com.bosch.masterdata.api.domain.dto.MaterialBinDTO;
import com.bosch.masterdata.api.domain.vo.MaterialBinVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 物料库位分配策略Mapper接口
 * 
 * @author xuhao
 * @date 2022-09-22
 */
@Mapper
@Repository("materialBinMapper")
public interface MaterialBinMapper extends BaseMapper<MaterialBin>
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
     * 删除物料库位分配策略
     * 
     * @param id 物料库位分配策略主键
     * @return 结果
     */
    public int deleteMaterialBinById(Long id);

    /**
     * 批量删除物料库位分配策略
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMaterialBinByIds(Long[] ids);
}
