package com.bosch.masterdata.mapper;

import java.util.List;
import com.bosch.masterdata.api.domain.Pallet;

/**
 * 托盘Mapper接口
 * 
 * @author xuhao
 * @date 2022-09-22
 */
public interface PalletMapper 
{
    /**
     * 查询托盘
     * 
     * @param id 托盘主键
     * @return 托盘
     */
    public Pallet selectPalletById(Long id);

    /**
     * 查询托盘列表
     * 
     * @param pallet 托盘
     * @return 托盘集合
     */
    public List<Pallet> selectPalletList(Pallet pallet);

    /**
     * 新增托盘
     * 
     * @param pallet 托盘
     * @return 结果
     */
    public int insertPallet(Pallet pallet);

    /**
     * 修改托盘
     * 
     * @param pallet 托盘
     * @return 结果
     */
    public int updatePallet(Pallet pallet);

    /**
     * 删除托盘
     * 
     * @param id 托盘主键
     * @return 结果
     */
    public int deletePalletById(Long id);

    /**
     * 批量删除托盘
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePalletByIds(Long[] ids);
}
