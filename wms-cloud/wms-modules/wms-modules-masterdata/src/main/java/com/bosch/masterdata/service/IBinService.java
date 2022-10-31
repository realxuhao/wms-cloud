package com.bosch.masterdata.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.masterdata.api.domain.Area;
import com.bosch.masterdata.api.domain.Bin;
import com.bosch.masterdata.api.domain.dto.AreaDTO;
import com.bosch.masterdata.api.domain.dto.BinDTO;
import com.bosch.masterdata.api.domain.vo.BinVO;

/**
 * 库位Service接口
 * 
 * @author xuhao
 * @date 2022-09-26
 */
public interface IBinService extends IService<Bin>
{
    /**
     * 查询库位
     * 
     * @param id 库位主键
     * @return 库位
     */
    public Bin selectBinById(Long id);
    /**
     * 查询库位
     *
     * @param
     * @return 库位
     */
    public List<Bin>  selectBinByFrameId(Long frameId);

    public BinVO selectBinVOById(Long id);

    public BinVO selectBinVOByCode(String code);

    /**
     * 查询库位列表
     * 
     * @param bin 库位
     * @return 库位集合
     */
    public List<Bin> selectBinList(Bin bin);

    public List<BinVO> selectBinList(BinDTO binDTO);
    /**
     * 新增库位
     * 
     * @param bin 库位
     * @return 结果
     */
    public int insertBin(Bin bin);

    public int insertBin(BinDTO binDTO);
    /**
     * 修改库位
     * 
     * @param bin 库位
     * @return 结果
     */
    public int updateBin(Bin bin);
    public int updateBin(BinDTO binDTO);
    /**
     * 批量删除库位
     * 
     * @param ids 需要删除的库位主键集合
     * @return 结果
     */
    public int deleteBinByIds(Long[] ids);

    /**
     * 删除库位信息
     * 
     * @param id 库位主键
     * @return 结果
     */
    public int deleteBinById(Long id);

    public Map<String,Long> getTypeMap(List<String> codes);

    /**
     * 重复校验
     * @param codes
     * @return
     */
    public boolean validList(List<String> codes);

    /**
     * 赋值
     * @param dtos
     * @return
     */
    public List<BinDTO> setValue(List<BinDTO> dtos);
}
