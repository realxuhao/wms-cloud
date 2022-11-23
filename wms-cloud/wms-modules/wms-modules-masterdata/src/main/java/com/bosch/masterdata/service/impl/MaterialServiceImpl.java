package com.bosch.masterdata.service.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.masterdata.api.domain.MaterialType;
import com.bosch.masterdata.api.domain.Pallet;
import com.bosch.masterdata.api.domain.dto.MaterialDTO;
import com.bosch.masterdata.api.domain.vo.MaterialVO;
import com.bosch.masterdata.mapper.MaterialTypeMapper;
import com.bosch.masterdata.mapper.PalletMapper;
import com.bosch.masterdata.service.IMaterialTypeService;
import com.bosch.masterdata.utils.BeanConverUtil;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.security.utils.SecurityUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.ibatis.binding.MapperMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bosch.masterdata.mapper.MaterialMapper;
import com.bosch.masterdata.api.domain.Material;
import com.bosch.masterdata.service.IMaterialService;

import javax.annotation.Resource;

/**
 * 物料信息Service业务层处理
 *
 * @author xuhao
 * @date 2022-09-22
 */
@Service
public class MaterialServiceImpl extends ServiceImpl<MaterialMapper, Material> implements IMaterialService {
    @Resource
    private MaterialMapper materialMapper;

    @Resource
    private MaterialTypeMapper materialTypeMapper;

    @Resource
    private PalletMapper palletMapper;
    @Autowired
    private IMaterialTypeService materialTypeService;

    /**
     * 查询物料信息
     *
     * @param id 物料信息主键
     * @return 物料信息
     */
    @Override
    public Material selectMaterialById(Long id) {
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
    public List<Material> validMaterialList(Material material) {
        return materialMapper.selectMaterialList(material);
    }

    @Override
    public List<MaterialVO> selectMaterialVOList(MaterialDTO materialDTO) {
        return materialMapper.selectMaterialVOList(materialDTO);
    }


    /**
     * 新增物料DTO
     *
     * @param materialDTO 物料信息
     * @return 结果
     */
    @Override
    public int insertMaterialDTO(MaterialDTO materialDTO) {
        if (ObjectUtils.isNotEmpty(materialDTO) && ObjectUtils.isNotEmpty(materialDTO.getPalletId())) {
            Pallet pallet = palletMapper.selectPalletById(materialDTO.getPalletId());
            if (pallet != null) {
                materialDTO.setPalletType(pallet.getType());
            }
        }
        Material material = BeanConverUtil.conver(materialDTO, Material.class);
        material.setCreateTime(DateUtils.getNowDate());
        material.setCreateBy(SecurityUtils.getUsername());
        return materialMapper.insertMaterial(material);
    }


    @Override
    public int updateMaterial(MaterialDTO materialDTO) {
        if (ObjectUtils.isNotEmpty(materialDTO) && ObjectUtils.isNotEmpty(materialDTO.getPalletId())) {
            Pallet pallet = palletMapper.selectPalletById(materialDTO.getPalletId());
            if (pallet != null) {
                materialDTO.setPalletType(pallet.getType());
            }
        }
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
    public int deleteMaterialByIds(Long[] ids) {
        return materialMapper.deleteMaterialByIds(ids);
    }

    /**
     * 删除物料信息信息
     *
     * @param id 物料信息主键
     * @return 结果
     */
    @Override
    public int deleteMaterialById(Long id) {
        return materialMapper.deleteMaterialById(id);
    }

    @Override
    public MaterialVO selectMaterialVOBymaterialCode(String materialCode) {
        return materialMapper.selectMaterialVOByMaterialCode(materialCode);
    }

    @Override
    public boolean validMaterialList(List<MaterialDTO> materials) {
        return materialMapper.validateRecord(materials) > 0;
    }

    @Override
    public Map<String, Long> getTypeMap(List<String> codes) {
        Map<String, Long> collect = new HashMap<>();
        LambdaQueryWrapper<MaterialType> queryWrapper = new LambdaQueryWrapper<MaterialType>();
        queryWrapper.in(MaterialType::getCode, codes);
        List<MaterialType> materialTypes = materialTypeMapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(materialTypes)) {
            collect = materialTypes.stream().collect(Collectors.toMap(MaterialType::getCode, MaterialType::getId));
        }
        return collect;
    }

    public Map<String, Long> getPalletMap(List<String> codes) {
        Map<String, Long> collect = new HashMap<>();
        LambdaQueryWrapper<Pallet> queryWrapper = new LambdaQueryWrapper<Pallet>();
        queryWrapper.in(Pallet::getType, codes);
        List<Pallet> pallets = palletMapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(pallets)) {
            collect = pallets.stream().collect(Collectors.toMap(Pallet::getType, Pallet::getId));
        }
        return collect;
    }

    @Override
    public List<MaterialDTO> setMaterialList(List<MaterialDTO> materialDTOList) {
        //获取物料类型集合
        List<String> types =
                materialDTOList.stream().map(MaterialDTO::getMaterialType).collect(Collectors.toList());
        //获取物料类型map
        Map<String, Long> typeMap = getTypeMap(types);
        //获取托盘类型
        List<String> palletTypes = materialDTOList.stream().map(MaterialDTO::getPalletType).collect(Collectors.toList());
        Map<String, Long> palletMap = getPalletMap(palletTypes);
        //绑定物料类型id
        materialDTOList.forEach(x -> {
            //绑定物料类型id
            if (typeMap.get(x.getMaterialType()) == null) {
                throw new ServiceException("Excel中包含主数据中不存在的物料类型：" + x.getMaterialType());
            }
            x.setMaterialTypeId(typeMap.get(x.getMaterialType()));
            //托盘id
            if (palletMap.get(x.getPalletType()) == null) {
                throw new ServiceException("Excel中包含主数据中不存在的托盘类型：" + x.getPalletType());
            }
            x.setPalletId(typeMap.get(x.getPalletType()));
        });
        return materialDTOList;
    }


}
