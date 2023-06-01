package com.bosch.masterdata.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.masterdata.api.domain.Area;
import com.bosch.masterdata.api.domain.Bin;
import com.bosch.masterdata.api.domain.dto.BinDTO;
import com.bosch.masterdata.api.domain.vo.BinVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 库位Mapper接口
 * 
 * @author xuhao
 * @date 2022-09-26
 */
@Mapper
@Repository("binMapper")
public interface BinMapper extends BaseMapper<Bin>
{
    /**
     * 查询库位
     * 
     * @param id 库位主键
     * @return 库位
     */
    public Bin selectBinById(Long id);

    public BinVO selectBinVOById(Long id);

    public BinVO selectBinVOByCode(String code);

    public List<BinVO> selectBinVOByFrameType(String code);

    public List<BinVO> selectBinVOByFrameTypeAndWare(String code,String ware);
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
