package com.bosch.masterdata.service.impl;

import java.util.List;

import com.bosch.masterdata.api.domain.dto.PalletDTO;
import com.bosch.masterdata.api.domain.vo.PalletVO;
import com.bosch.masterdata.utils.BeanConverUtil;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bosch.masterdata.mapper.PalletMapper;
import com.bosch.masterdata.api.domain.Pallet;
import com.bosch.masterdata.service.IPalletService;

/**
 * 托盘Service业务层处理
 * 
 * @author xuhao
 * @date 2022-09-22
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

    @Override
    public List<PalletVO> selectPalletList(PalletDTO palletDTO) {
        Pallet pallet = BeanConverUtil.conver(palletDTO, Pallet.class);

        return BeanConverUtil.converList(palletMapper.selectPalletList(pallet),PalletVO.class);
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

    @Override
    public int insertPallet(PalletDTO palletDTO) {
        Pallet pallet = BeanConverUtil.conver(palletDTO, Pallet.class);
        pallet.setCreateTime(DateUtils.getNowDate());
        pallet.setCreateBy(SecurityUtils.getUsername());
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

    @Override
    public int updatePallet(PalletDTO palletDTO) {
        Pallet pallet = BeanConverUtil.conver(palletDTO, Pallet.class);
        pallet.setUpdateTime(DateUtils.getNowDate());
        pallet.setUpdateBy(SecurityUtils.getUsername());
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
