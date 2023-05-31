package com.bosch.masterdata.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.masterdata.api.domain.Area;
import com.bosch.masterdata.api.domain.Frame;
import com.bosch.masterdata.api.domain.Material;
import com.bosch.masterdata.api.domain.dto.FrameDTO;
import com.bosch.masterdata.api.domain.dto.MaterialBinDTO;
import com.bosch.masterdata.api.domain.vo.MaterialBinVO;
import com.bosch.masterdata.mapper.FrameMapper;
import com.bosch.masterdata.mapper.MaterialMapper;
import com.bosch.masterdata.utils.BeanConverUtil;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.exception.ServiceException;
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
public class MaterialBinServiceImpl extends ServiceImpl<MaterialBinMapper, MaterialBin> implements IMaterialBinService {
    @Autowired
    private MaterialBinMapper materialBinMapper;
    @Autowired
    private MaterialMapper materialMapper;
    @Autowired
    private FrameMapper frameMapper;

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
        LambdaQueryWrapper<Material> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Material::getId,materialBin.getMaterialId());
        Material material = materialMapper.selectOne(lambdaQueryWrapper);
        if(material==null){
            throw  new ServiceException("通过物料id未找到物料code");
        }
        materialBin.setMaterialCode(material.getCode());
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
        materialBin.setUpdateBy(SecurityUtils.getUsername());
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

    @Override
    public Map<String, Long> getTypeMap(List<String> codes, int i) {
        Map<String, Long> collect = new HashMap<>();
        if (i == 1) {
            LambdaQueryWrapper<Frame> queryWrapper = new LambdaQueryWrapper<Frame>();
            queryWrapper.in(Frame::getCode, codes);
            List<Frame> frames = frameMapper.selectList(queryWrapper);
            if (CollectionUtils.isNotEmpty(frames)) {
                collect = frames.stream().collect(Collectors.toMap(Frame::getCode, Frame::getId));
            }
            return collect;
        } else {
            LambdaQueryWrapper<Material> queryWrapper = new LambdaQueryWrapper<Material>();
            queryWrapper.in(Material::getCode, codes);
            List<Material> materials = materialMapper.selectList(queryWrapper);
            if (CollectionUtils.isNotEmpty(materials)) {
                collect = materials.stream().collect(Collectors.toMap(Material::getCode, Material::getId));
            }
            return collect;
        }
    }

    public boolean validList(List<MaterialBinDTO> dtos) {
        for (MaterialBinDTO dto : dtos) {
            LambdaQueryWrapper<MaterialBin> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(MaterialBin::getFrameTypeCode, dto.getFrameTypeCode());
            lambdaQueryWrapper.eq(MaterialBin::getMaterialCode, dto.getMaterialCode());
            MaterialBin materialBin = materialBinMapper.selectOne(lambdaQueryWrapper);
            if (materialBin != null) {
                return false;
            }
        }
        return true;
    }

    public boolean validOne(MaterialBinDTO dto) {

        LambdaQueryWrapper<MaterialBin> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MaterialBin::getFrameTypeCode, dto.getFrameTypeCode());
        lambdaQueryWrapper.eq(MaterialBin::getMaterialId, dto.getMaterialId());
        Integer integer = materialBinMapper.selectCount(lambdaQueryWrapper);
        if (integer > 0) {
            return false;
        }

        return true;
    }

    public List<MaterialBinDTO> setValue(List<MaterialBinDTO> dtos) {
        //获取集合
//        List<String> frames =
//                dtos.stream().map(MaterialBinDTO::getFrameCode).collect(Collectors.toList());
        List<String> materials =
                dtos.stream().map(MaterialBinDTO::getMaterialCode).collect(Collectors.toList());
        //获取map
//        Map<String,Long> framesMap = getTypeMap(frames,1);
        Map<String, Long> materialsMap = getTypeMap(materials, 2);
        //绑定id
        dtos.forEach(x -> {
            if (materialsMap.get(x.getMaterialCode()) == null) {
                throw new ServiceException("包含不存在的物料代码:"+x.getMaterialCode());
            }
            x.setMaterialId(materialsMap.get(x.getMaterialCode()));
        });
        return dtos;
    }

    @Override
    public List<MaterialBinVO> getListByMaterial(String materialCode) {

        List<MaterialBinVO> materialBinVOS = materialBinMapper.selectByWareCode(materialCode,
                SecurityUtils.getWareCode());
        return materialBinVOS;
    }

    @Override
    public List<MaterialBinVO> getListByMaterial(String materialCode,String wareCode) {

        List<MaterialBinVO> materialBinVOS = materialBinMapper.selectByWareCode(materialCode,
                wareCode);
        return materialBinVOS;
    }
}
