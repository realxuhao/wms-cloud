package com.bosch.maindata.service.impl;

import java.util.List;

import com.bosch.maindata.service.IPalletService;
import com.ruoyi.common.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bosch.maindata.mapper.PalletMapper;
import com.bosch.maindata.domain.Pallet;

/**
 * 托盘Service业务层处理
 * 
 * @author xuhao
 * @date 2022-09-21
 */
@Service
public class PalletServiceImpl implements IPalletService
{
    @Autowired
    private PalletMapper palletMapper;

    /**
     * 查询托盘
     * 
     * @param id 托盘主键
     * @return 托盘
     */
    @Override
    public Pallet selectPalletById(Long id)
    {
        return palletMapper.selectPalletById(id);
    }

    /**
     * 查询托盘列表
     * 
     * @param pallet 托盘
     * @return 托盘
     */
    @Override
    public List<Pallet> selectPalletList(Pallet pallet)
    {
        return palletMapper.selectPalletList(pallet);
    }

    /**
     * 新增托盘
     * 
     * @param pallet 托盘
     * @return 结果
     */
    @Override
    public int insertPallet(Pallet pallet)
    {
        pallet.setCreateTime(DateUtils.getNowDate());
        return palletMapper.insertPallet(pallet);
    }

    /**
     * 修改托盘
     * 
     * @param pallet 托盘
     * @return 结果
     */
    @Override
    public int updatePallet(Pallet pallet)
    {
        pallet.setUpdateTime(DateUtils.getNowDate());
        return palletMapper.updatePallet(pallet);
    }

    /**
     * 批量删除托盘
     * 
     * @param ids 需要删除的托盘主键
     * @return 结果
     */
    @Override
    public int deletePalletByIds(Long[] ids)
    {
        return palletMapper.deletePalletByIds(ids);
    }

    /**
     * 删除托盘信息
     * 
     * @param id 托盘主键
     * @return 结果
     */
    @Override
    public int deletePalletById(Long id)
    {
        return palletMapper.deletePalletById(id);
    }
}
