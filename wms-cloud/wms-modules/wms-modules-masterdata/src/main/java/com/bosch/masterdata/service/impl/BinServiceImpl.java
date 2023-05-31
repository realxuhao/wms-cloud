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
import com.bosch.masterdata.api.domain.Ware;
import com.bosch.masterdata.api.domain.dto.AreaDTO;
import com.bosch.masterdata.api.domain.dto.BinDTO;
import com.bosch.masterdata.api.domain.vo.BinVO;
import com.bosch.masterdata.mapper.AreaMapper;
import com.bosch.masterdata.mapper.FrameMapper;
import com.bosch.masterdata.utils.BeanConverUtil;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bosch.masterdata.mapper.BinMapper;
import com.bosch.masterdata.api.domain.Bin;
import com.bosch.masterdata.service.IBinService;

/**
 * 库位Service业务层处理
 * 
 * @author xuhao
 * @date 2022-09-26
 */
@Service
public class BinServiceImpl extends ServiceImpl<BinMapper, Bin> implements IBinService
{
    @Autowired
    private BinMapper binMapper;

    @Autowired
    private FrameMapper frameMapper;
    /**
     * 查询库位
     * 
     * @param id 库位主键
     * @return 库位
     */
    @Override
    public Bin selectBinById(Long id)
    {
        return binMapper.selectBinById(id);
    }

    public List<BinVO> selectBinVOByFrameType(String code)
    {
        return binMapper.selectBinVOByFrameType(code);
    }
    @Override
    public List<Bin>  selectBinByFrameId(Long frameId)
    {
        LambdaQueryWrapper<Bin> lambdaQueryWrapper=new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(Bin::getFrameId,frameId);
        List<Bin> bins = binMapper.selectList(lambdaQueryWrapper);
        return bins;
    }

    @Override
    public BinVO selectBinVOById(Long id) {
        return binMapper.selectBinVOById(id);
    }

    @Override
    public BinVO selectBinVOByCode(String code) {
        return binMapper.selectBinVOByCode(code);
    }
    /**
     * 查询库位列表
     * 
     * @param bin 库位
     * @return 库位
     */
    @Override
    public List<Bin> selectBinList(Bin bin)
    {
        return binMapper.selectBinList(bin);
    }

    @Override
    public List<BinVO> selectBinList(BinDTO binDTO) {

        return binMapper.selectBinVOList(binDTO);
    }

    /**
     * 新增库位
     * 
     * @param bin 库位
     * @return 结果
     */
    @Override
    public int insertBin(Bin bin)
    {
        bin.setCreateTime(DateUtils.getNowDate());
        return binMapper.insertBin(bin);
    }

    @Override
    public int insertBin(BinDTO binDTO) {
        Bin bin = BeanConverUtil.conver(binDTO, Bin.class);
        bin.setCreateBy(SecurityUtils.getUsername());
        bin.setCreateTime(DateUtils.getNowDate());
        return binMapper.insertBin(bin);
    }

    /**
     * 修改库位
     * 
     * @param bin 库位
     * @return 结果
     */
    @Override
    public int updateBin(Bin bin)
    {
        bin.setUpdateTime(DateUtils.getNowDate());
        return binMapper.updateBin(bin);
    }

    @Override
    public int updateBin(BinDTO binDTO) {
        Bin bin = BeanConverUtil.conver(binDTO, Bin.class);
        bin.setUpdateBy(SecurityUtils.getUsername());
        bin.setUpdateTime(DateUtils.getNowDate());
        return binMapper.updateBin(bin);
    }

    /**
     * 批量删除库位
     * 
     * @param ids 需要删除的库位主键
     * @return 结果
     */
    @Override
    public int deleteBinByIds(Long[] ids)
    {
        return binMapper.deleteBinByIds(ids);
    }

    /**
     * 删除库位信息
     * 
     * @param id 库位主键
     * @return 结果
     */
    @Override
    public int deleteBinById(Long id)
    {
        return binMapper.deleteBinById(id);
    }

    @Override
    public Map<String,Long> getTypeMap(List<String> codes) {
        Map<String,Long> collect=new HashMap<>();
        LambdaQueryWrapper<Frame> queryWrapper=new LambdaQueryWrapper<Frame>();
        queryWrapper.in(Frame::getCode,codes);
        List<Frame> types = frameMapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(types)){
            collect = types.stream().collect(Collectors.toMap(Frame::getCode,Frame::getId));
        }
        return  collect;
    }

    public boolean validList(List<String> codes) {
        QueryWrapper<Bin> wrapper=new QueryWrapper<>();
        wrapper.in("code",codes);
        return  binMapper.selectCount(wrapper)>0;
    }
    public List<BinDTO> setValue(List<BinDTO> dtos) {
        //获取集合
        List<String> types =
                dtos.stream().map(BinDTO::getFrameCode).collect(Collectors.toList());
        //获取map
        Map<String,Long> typeMap = getTypeMap(types);
        //绑定id
        dtos.forEach(x->{
            if (typeMap.get(x.getFrameCode())==null){
                throw new ServiceException("包含不存在的跨Code:"+x.getFrameCode());
            }
            x.setFrameId(typeMap.get(x.getFrameCode()));
        });
        return dtos;
    }
}
