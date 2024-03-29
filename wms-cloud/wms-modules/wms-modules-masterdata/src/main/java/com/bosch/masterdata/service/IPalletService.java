package com.bosch.masterdata.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.masterdata.api.domain.Material;
import com.bosch.masterdata.api.domain.Pallet;
import com.bosch.masterdata.api.domain.dto.PalletDTO;
import com.bosch.masterdata.api.domain.vo.PalletVO;

/**
 * 托盘Service接口
 * 
 * @author xuhao
 * @date 2022-09-22
 */
public interface IPalletService extends IService<Pallet>
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

    public List<PalletVO> selectPalletList(PalletDTO palletDTO);
    /**
     * 新增托盘
     * 
     * @param pallet 托盘
     * @return 结果
     */
    public int insertPallet(Pallet pallet);

    public int insertPallet(PalletDTO palletDTO);
    /**
     * 修改托盘
     * 
     * @param pallet 托盘
     * @return 结果
     */
    public int updatePallet(Pallet pallet);

    public int updatePallet(PalletDTO palletDTO);
    /**
     * 批量删除托盘
     * 
     * @param ids 需要删除的托盘主键集合
     * @return 结果
     */
    public int deletePalletByIds(Long[] ids);

    /**
     * 删除托盘信息
     * 
     * @param id 托盘主键
     * @return 结果
     */
    public int deletePalletById(Long id);

    Pallet selectPalletByType(String palletType);

    //验证是否有相同数据
    boolean validDTO(List<PalletDTO> dtos);

    //验证是否有相同数据
    boolean validDTO(PalletDTO dto);

}
