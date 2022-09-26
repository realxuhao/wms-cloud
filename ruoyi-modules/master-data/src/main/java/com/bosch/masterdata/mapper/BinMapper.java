package com.bosch.masterdata.mapper;

import java.util.List;
import com.bosch.masterdata.domain.Bin;
import com.bosch.masterdata.domain.dto.BinDTO;
import com.bosch.masterdata.domain.vo.BinVO;

/**
 * 库位Mapper接口
 * 
 * @author xuhao
 * @date 2022-09-26
 */
public interface BinMapper 
{
    /**
     * 查询库位
     * 
     * @param id 库位主键
     * @return 库位
     */
    public Bin selectBinById(Long id);

    public BinVO selectBinVOById(Long id);
    /**
     * 查询库位列表
     * 
     * @param bin 库位
     * @return 库位集合
     */
    public List<Bin> selectBinList(Bin bin);

    public List<BinVO> selectBinVOList(BinDTO binDTO);
    /**
     * 新增库位
     * 
     * @param bin 库位
     * @return 结果
     */
    public int insertBin(Bin bin);

    /**
     * 修改库位
     * 
     * @param bin 库位
     * @return 结果
     */
    public int updateBin(Bin bin);

    /**
     * 删除库位
     * 
     * @param id 库位主键
     * @return 结果
     */
    public int deleteBinById(Long id);

    /**
     * 批量删除库位
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBinByIds(Long[] ids);
}
