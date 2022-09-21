package com.bosch.maindata.service.impl;

import java.util.List;
import com.ruoyi.common.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bosch.maindata.mapper.CellMapper;
import com.bosch.maindata.domain.Cell;
import com.bosch.maindata.service.ICellService;

/**
 * 部门Service业务层处理
 * 
 * @author xuhao
 * @date 2022-09-21
 */
@Service
public class CellServiceImpl implements ICellService 
{
    @Autowired
    private CellMapper cellMapper;

    /**
     * 查询部门
     * 
     * @param id 部门主键
     * @return 部门
     */
    @Override
    public Cell selectCellById(Long id)
    {
        return cellMapper.selectCellById(id);
    }

    /**
     * 查询部门列表
     * 
     * @param cell 部门
     * @return 部门
     */
    @Override
    public List<Cell> selectCellList(Cell cell)
    {
        return cellMapper.selectCellList(cell);
    }

    /**
     * 新增部门
     * 
     * @param cell 部门
     * @return 结果
     */
    @Override
    public int insertCell(Cell cell)
    {
        cell.setCreateTime(DateUtils.getNowDate());
        return cellMapper.insertCell(cell);
    }

    /**
     * 修改部门
     * 
     * @param cell 部门
     * @return 结果
     */
    @Override
    public int updateCell(Cell cell)
    {
        cell.setUpdateTime(DateUtils.getNowDate());
        return cellMapper.updateCell(cell);
    }

    /**
     * 批量删除部门
     * 
     * @param ids 需要删除的部门主键
     * @return 结果
     */
    @Override
    public int deleteCellByIds(Long[] ids)
    {
        return cellMapper.deleteCellByIds(ids);
    }

    /**
     * 删除部门信息
     * 
     * @param id 部门主键
     * @return 结果
     */
    @Override
    public int deleteCellById(Long id)
    {
        return cellMapper.deleteCellById(id);
    }
}
