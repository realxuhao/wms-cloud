package com.bosch.masterdata.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.masterdata.api.domain.Material;
import com.bosch.masterdata.api.domain.dto.MaterialBinDTO;
import com.bosch.masterdata.api.domain.vo.MaterialBinVO;
import com.bosch.masterdata.mapper.MaterialMapper;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bosch.masterdata.mapper.MaterialBinMapper;
import com.bosch.masterdata.api.domain.MaterialBin;
import com.bosch.masterdata.service.IMaterialBinService;

/**
 * 物料库位分配策略Service业务层处理
 *
 * @author xuhao
 * @date 2022-09-22
 */
@Service
public class MaterialBinServiceImpl  extends ServiceImpl<MaterialBinMapper, MaterialBin> implements   IMaterialBinService {
    @Autowired
    private MaterialBinMapper materialBinMapper;

    /**
     * 查询物料库位分配策略
     *
     * @param id 物料库位分配策略主键
     * @return 物料库位分配策略
     */
    @Override
    public MaterialBinVO selectMaterialBinById(Long id) {
        return materialBinMapper.selectMaterialBinById(id);
    }

    /**
     * 查询物料库位分配策略列表
     *
     * @param materialBinDTO 物料库位分配策略
     * @return 物料库位分配策略
     */
    @Override
    public List<MaterialBinVO> selectMaterialBinList(MaterialBinDTO materialBinDTO) {
        return materialBinMapper.selectMaterialBinList(materialBinDTO);
    }

    /**
     * 新增物料库位分配策略
     *
     * @param materialBin 物料库位分配策略
     * @return 结果
     */
    @Override
    public int insertMaterialBin(MaterialBin materialBin) {
        materialBin.setCreateTime(DateUtils.getNowDate());
        materialBin.setCreateBy(SecurityUtils.getUsername());
        return materialBinMapper.insertMaterialBin(materialBin);
    }

    /**
     * 修改物料库位分配策略
     *
     * @param materialBin 物料库位分配策略
     * @return 结果
     */
    @Override
    public int updateMaterialBin(MaterialBin materialBin) {
        materialBin.setUpdateTime(DateUtils.getNowDate());
        return materialBinMapper.updateMaterialBin(materialBin);
    }

    /**
     * 批量删除物料库位分配策略
     *
     * @param ids 需要删除的物料库位分配策略主键
     * @return 结果
     */
    @Override
    public int deleteMaterialBinByIds(Long[] ids) {
        return materialBinMapper.deleteMaterialBinByIds(ids);
    }

    /**
     * 删除物料库位分配策略信息
     *
     * @param id 物料库位分配策略主键
     * @return 结果
     */
    @Override
    public int deleteMaterialBinById(Long id) {
        return materialBinMapper.deleteMaterialBinById(id);
    }
}
