package com.bosch.masterdata.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.masterdata.api.domain.Area;
import com.bosch.masterdata.api.domain.SupplierInfo;
import com.bosch.masterdata.api.domain.dto.AreaDTO;
import com.bosch.masterdata.api.domain.vo.AreaVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 区域Mapper接口
 * 
 * @author xuhao
 * @date 2022-09-26
 */
@Mapper
@Repository("areaMapper")
public interface AreaMapper extends BaseMapper<Area>
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

    public List<AreaVO> selectAreaVOList(AreaDTO areaDTO);
    /**
     * 新增区域
     * 
     * @param area 区域
     * @return 结果
     */
    public int insertArea(Area area);

    /**
     * 修改区域
     * 
     * @param area 区域
     * @return 结果
     */
    public int updateArea(Area area);

    /**
     * 删除区域
     * 
     * @param id 区域主键
     * @return 结果
     */
    public int deleteAreaById(Integer id);

    /**
     * 批量删除区域
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAreaByIds(Integer[] ids);
}
