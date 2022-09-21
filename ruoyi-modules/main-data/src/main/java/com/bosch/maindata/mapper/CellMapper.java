package com.bosch.maindata.mapper;

import java.util.List;
import com.bosch.maindata.domain.Cell;

/**
 * 部门Mapper接口
 * 
 * @author xuhao
 * @date 2022-09-21
 */
public interface CellMapper 
{
    /**
     * 查询部门
     * 
     * @param id 部门主键
     * @return 部门
     */
    public Cell selectCellById(Long id);

    /**
     * 查询部门列表
     * 
     * @param cell 部门
     * @return 部门集合
     */
    public List<Cell> selectCellList(Cell cell);

    /**
     * 新增部门
     * 
     * @param cell 部门
     * @return 结果
     */
    public int insertCell(Cell cell);

    /**
     * 修改部门
     * 
     * @param cell 部门
     * @return 结果
     */
    public int updateCell(Cell cell);

    /**
     * 删除部门
     * 
     * @param id 部门主键
     * @return 结果
     */
    public int deleteCellById(Long id);

    /**
     * 批量删除部门
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCellByIds(Long[] ids);
}
