package com.bosch.maindata.service;

import java.util.List;
import com.bosch.maindata.domain.Cell;

/**
 * 部门Service接口
 * 
 * @author xuhao
 * @date 2022-09-21
 */
public interface ICellService 
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
     * 批量删除部门
     * 
     * @param ids 需要删除的部门主键集合
     * @return 结果
     */
    public int deleteCellByIds(Long[] ids);

    /**
     * 删除部门信息
     * 
     * @param id 部门主键
     * @return 结果
     */
    public int deleteCellById(Long id);
}
