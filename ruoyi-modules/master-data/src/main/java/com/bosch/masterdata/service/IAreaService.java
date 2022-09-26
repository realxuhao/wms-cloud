package com.bosch.masterdata.service;

import java.util.List;
import com.bosch.masterdata.domain.Area;
import com.bosch.masterdata.domain.dto.AreaDTO;
import com.bosch.masterdata.domain.vo.AreaVO;

/**
 * 区域Service接口
 * 
 * @author xuhao
 * @date 2022-09-26
 */
public interface IAreaService 
{
    /**
     * 查询区域
     * 
     * @param id 区域主键
     * @return 区域
     */
    public Area selectAreaById(Integer id);

    /**
     * 查询区域列表
     * 
     * @param area 区域
     * @return 区域集合
     */
    public List<Area> selectAreaList(Area area);

    /**
     * 查询区域VO列表
     *
     * @param areaDTO 区域
     * @return 区域集合
     */
    public List<AreaVO> selectAreaVOList(AreaDTO areaDTO);

    /**
     * 新增区域
     * 
     * @param area 区域
     * @return 结果
     */
    public int insertArea(Area area);

    public int insertArea(AreaDTO areaDTO);
    /**
     * 修改区域
     * 
     * @param area 区域
     * @return 结果
     */
    public int updateArea(Area area);

    public int updateArea(AreaDTO areaDTO);
    /**
     * 批量删除区域
     * 
     * @param ids 需要删除的区域主键集合
     * @return 结果
     */
    public int deleteAreaByIds(Integer[] ids);

    /**
     * 删除区域信息
     * 
     * @param id 区域主键
     * @return 结果
     */
    public int deleteAreaById(Integer id);
}
