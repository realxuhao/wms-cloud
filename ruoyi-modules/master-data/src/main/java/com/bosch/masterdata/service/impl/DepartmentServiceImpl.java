package com.bosch.masterdata.service.impl;

import java.util.List;
import com.ruoyi.common.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bosch.masterdata.mapper.DepartmentMapper;
import com.bosch.masterdata.domain.Department;
import com.bosch.masterdata.service.IDepartmentService;

/**
 * 部门Service业务层处理
 * 
 * @author xuhao
 * @date 2022-09-22
 */
@Service
public class DepartmentServiceImpl implements IDepartmentService 
{
    @Autowired
    private DepartmentMapper departmentMapper;

    /**
     * 查询部门
     * 
     * @param id 部门主键
     * @return 部门
     */
    @Override
    public Department selectDepartmentById(Long id)
    {
        return departmentMapper.selectDepartmentById(id);
    }

    /**
     * 查询部门列表
     * 
     * @param department 部门
     * @return 部门
     */
    @Override
    public List<Department> selectDepartmentList(Department department)
    {
        return departmentMapper.selectDepartmentList(department);
    }

    /**
     * 新增部门
     * 
     * @param department 部门
     * @return 结果
     */
    @Override
    public int insertDepartment(Department department)
    {
        department.setCreateTime(DateUtils.getNowDate());
        return departmentMapper.insertDepartment(department);
    }

    /**
     * 修改部门
     * 
     * @param department 部门
     * @return 结果
     */
    @Override
    public int updateDepartment(Department department)
    {
        department.setUpdateTime(DateUtils.getNowDate());
        return departmentMapper.updateDepartment(department);
    }

    /**
     * 批量删除部门
     * 
     * @param ids 需要删除的部门主键
     * @return 结果
     */
    @Override
    public int deleteDepartmentByIds(Long[] ids)
    {
        return departmentMapper.deleteDepartmentByIds(ids);
    }

    /**
     * 删除部门信息
     * 
     * @param id 部门主键
     * @return 结果
     */
    @Override
    public int deleteDepartmentById(Long id)
    {
        return departmentMapper.deleteDepartmentById(id);
    }
}
