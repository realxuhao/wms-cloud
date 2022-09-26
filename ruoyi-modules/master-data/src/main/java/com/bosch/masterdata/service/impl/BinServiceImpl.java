package com.bosch.masterdata.service.impl;

import java.util.List;
import com.ruoyi.common.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bosch.masterdata.mapper.BinMapper;
import com.bosch.masterdata.domain.Bin;
import com.bosch.masterdata.service.IBinService;

/**
 * 库位Service业务层处理
 * 
 * @author xuhao
 * @date 2022-09-26
 */
@Service
public class BinServiceImpl implements IBinService 
{
    @Autowired
    private BinMapper binMapper;

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
}
