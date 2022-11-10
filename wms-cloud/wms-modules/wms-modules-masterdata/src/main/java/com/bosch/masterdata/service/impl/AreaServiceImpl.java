package com.bosch.masterdata.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.masterdata.api.domain.MaterialType;
import com.bosch.masterdata.api.domain.SupplierInfo;
import com.bosch.masterdata.api.domain.Ware;
import com.bosch.masterdata.api.domain.dto.AreaDTO;
import com.bosch.masterdata.api.domain.dto.MaterialDTO;
import com.bosch.masterdata.api.domain.vo.AreaVO;
import com.bosch.masterdata.mapper.SupplierInfoMapper;
import com.bosch.masterdata.mapper.WareMapper;
import com.bosch.masterdata.utils.BeanConverUtil;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bosch.masterdata.mapper.AreaMapper;
import com.bosch.masterdata.api.domain.Area;
import com.bosch.masterdata.service.IAreaService;

/**
 * 区域Service业务层处理
 * 
 * @author xuhao
 * @date 2022-09-26
 */
@Service
public class AreaServiceImpl extends ServiceImpl<AreaMapper, Area> implements IAreaService
{
    @Autowired
    private AreaMapper areaMapper;

    @Autowired
    private WareMapper wareMapper;
    /**
     * 查询区域
     * 
     * @param id 区域主键
     * @return 区域
     */
    @Override
    public Area selectAreaById(Integer id)
    {
        return areaMapper.selectAreaById(id);
    }

    /**
     * 查询区域列表
     * 
     * @param area 区域
     * @return 区域
     */
    @Override
    public List<Area> selectAreaList(Area area)
    {
        return areaMapper.selectAreaList(area);
    }

    @Override
    public List<AreaVO> selectAreaVOList(AreaDTO areaDTO) {
        Area area = BeanConverUtil.conver(areaDTO, Area.class);

        return BeanConverUtil.converList(areaMapper.selectAreaVOList(area),AreaVO.class);
    }

    /**
     * 新增区域
     * 
     * @param area 区域
     * @return 结果
     */
    @Override
    public int insertArea(Area area)
    {
        area.setCreateTime(DateUtils.getNowDate());
        return areaMapper.insertArea(area);
    }

    @Override
    public int insertArea(AreaDTO areaDTO) {
        Area area = BeanConverUtil.conver(areaDTO, Area.class);
        area.setCreateTime(DateUtils.getNowDate());
        area.setCreateBy(SecurityUtils.getUsername());
        return areaMapper.insertArea(area);
    }

    /**
     * 修改区域
     * 
     * @param area 区域
     * @return 结果
     */
    @Override
    public int updateArea(Area area)
    {
        area.setUpdateTime(DateUtils.getNowDate());
        return areaMapper.updateArea(area);
    }

    @Override
    public int updateArea(AreaDTO areaDTO) {
        Area area = BeanConverUtil.conver(areaDTO, Area.class);
        area.setUpdateBy(SecurityUtils.getUsername());
        area.setUpdateTime(DateUtils.getNowDate());
        return areaMapper.updateArea(area);
    }

    /**
     * 批量删除区域
     * 
     * @param ids 需要删除的区域主键
     * @return 结果
     */
    @Override
    public int deleteAreaByIds(Integer[] ids)
    {
        return areaMapper.deleteAreaByIds(ids);
    }

    /**
     * 删除区域信息
     * 
     * @param id 区域主键
     * @return 结果
     */
    @Override
    public int deleteAreaById(Integer id)
    {
        return areaMapper.deleteAreaById(id);
    }

    @Override
    public Map<String,Long> getTypeMap(List<String> codes) {
        Map<String,Long> collect=new HashMap<>();
        LambdaQueryWrapper<Ware> queryWrapper=new LambdaQueryWrapper<Ware>();
        queryWrapper.in(Ware::getCode,codes);
        List<Ware> types = wareMapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(types)){
            collect = types.stream().collect(Collectors.toMap(Ware::getCode,Ware::getId));
        }
        return  collect;
    }

    public boolean validList(List<String> codes) {
        QueryWrapper<Area> wrapper=new QueryWrapper<>();
        wrapper.in("code",codes);
        return  areaMapper.selectCount(wrapper)>0;
    }
    public List<AreaDTO> setValue(List<AreaDTO> dtos) {
        //获取集合
        List<String> types =
                dtos.stream().map(AreaDTO::getWareCode).collect(Collectors.toList());
        //获取map
        Map<String,Long> typeMap = getTypeMap(types);
        //绑定id
        dtos.forEach(x->{
            if (typeMap.get(x.getWareCode())==null){
                throw new ServiceException("包含不存在的仓库Code");
            }
            x.setWareId(typeMap.get(x.getWareCode()));
        });
        return dtos;
    }
}
