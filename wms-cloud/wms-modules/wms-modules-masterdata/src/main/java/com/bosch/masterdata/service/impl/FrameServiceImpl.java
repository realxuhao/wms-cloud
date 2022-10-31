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
import com.bosch.masterdata.api.domain.Material;
import com.bosch.masterdata.api.domain.Ware;
import com.bosch.masterdata.api.domain.dto.AreaDTO;
import com.bosch.masterdata.api.domain.dto.FrameDTO;
import com.bosch.masterdata.api.domain.vo.FrameVO;
import com.bosch.masterdata.mapper.AreaMapper;
import com.bosch.masterdata.mapper.MaterialMapper;
import com.bosch.masterdata.utils.BeanConverUtil;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bosch.masterdata.mapper.FrameMapper;
import com.bosch.masterdata.api.domain.Frame;
import com.bosch.masterdata.service.IFrameService;

/**
 * 跨Service业务层处理
 *
 * @author xuhao
 * @date 2022-09-26
 */
@Service
public class FrameServiceImpl extends ServiceImpl<FrameMapper, Frame>  implements IFrameService {
    @Autowired
    private FrameMapper frameMapper;

    @Autowired
    private AreaMapper areaMapper;
    /**
     * 查询跨
     *
     * @param id 跨主键
     * @return 跨
     */
    @Override
    public FrameVO selectFrameById(Long id) {
        return frameMapper.selectFrameById(id);
    }

    @Override
    public FrameVO selectFrameByCode(String code){
        return frameMapper.selectFrameByCode(code);
    }
    /**
     * 查询跨列表
     *
     * @param frameDTO 跨
     * @return 跨
     */
    @Override
    public List<FrameVO> selectFrameList(FrameDTO frameDTO) {
        return frameMapper.selectFrameList(frameDTO);
    }

    /**
     * 新增跨
     *
     * @param frameDTO 跨
     * @return 结果
     */
    @Override
    public int insertFrame(FrameDTO frameDTO) {
        Frame frame = BeanConverUtil.conver(frameDTO, Frame.class);
        frame.setCreateTime(DateUtils.getNowDate());
        frame.setCreateBy(SecurityUtils.getUsername());
        return frameMapper.insertFrame(frame);
    }

    /**
     * 修改跨
     *
     * @param frame 跨
     * @return 结果
     */
    @Override
    public int updateFrame(FrameDTO frameDTO) {
        Frame frame = BeanConverUtil.conver(frameDTO, Frame.class);
        frame.setUpdateTime(DateUtils.getNowDate());
        frame.setUpdateBy(SecurityUtils.getUsername());
        return frameMapper.updateFrame(frame);
    }

    /**
     * 批量删除跨
     *
     * @param ids 需要删除的跨主键
     * @return 结果
     */
    @Override
    public int deleteFrameByIds(Long[] ids) {
        return frameMapper.deleteFrameByIds(ids);
    }

    /**
     * 删除跨信息
     *
     * @param id 跨主键
     * @return 结果
     */
    @Override
    public int deleteFrameById(Long id) {
        return frameMapper.deleteFrameById(id);
    }

    @Override
    public Map<String,Long> getTypeMap(List<String> codes) {
        Map<String,Long> collect=new HashMap<>();
        LambdaQueryWrapper<Area> queryWrapper=new LambdaQueryWrapper<Area>();
        queryWrapper.in(Area::getCode,codes);
        List<Area> types = areaMapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(types)){
            collect = types.stream().collect(Collectors.toMap(Area::getCode,Area::getId));
        }
        return  collect;
    }

    public boolean validList(List<String> codes) {
        QueryWrapper<Frame> wrapper=new QueryWrapper<>();
        wrapper.in("code",codes);
        return  frameMapper.selectCount(wrapper)>0;
    }


    public List<FrameDTO> setValue(List<FrameDTO> dtos) {
        //获取集合
        List<String> types =
                dtos.stream().map(FrameDTO::getAreaCode).collect(Collectors.toList());
        //获取map
        Map<String,Long> typeMap = getTypeMap(types);
        //绑定id
        dtos.forEach(x->{
            if (typeMap.get(x.getAreaCode())==null){
                throw new ServiceException("包含不存在的区域Code");
            }
            x.setAreaId(typeMap.get(x.getAreaCode()));
        });
        return dtos;
    }
}
