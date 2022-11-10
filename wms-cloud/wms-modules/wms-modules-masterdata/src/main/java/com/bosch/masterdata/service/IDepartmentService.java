package com.bosch.masterdata.service;

import java.util.List;
import com.bosch.masterdata.api.domain.Department;
import com.bosch.masterdata.api.domain.vo.DepartmentVO;

/**
 * 部门Service接口
 * 
 * @author xuhao
 * @date 2022-09-22
 */
public interface IDepartmentService
{
    /**
     * 查询部门
     * 
     * @param id 部门主键
     * @return 部门
     */
    public Department selectDepartmentById(Long id);

    /**
     * 查询部门列表
     * 
     * @param department 部门
     * @return 部门集合
     */
    public List<Department> selectDepartmentList(Department department);

    /**
     * 新增部门
     * 
     * @param department 部门
     * @return 结果
     */
    public int insertDepartment(Department department);

    /**
     * 修改部门
     * 
     * @param department 部门
     * @return 结果
     */
    public int updateDepartment(Department department);

    /**
     * 批量删除部门
     * 
     * @param ids 需要删除的部门主键集合
     * @return 结果
     */
    public int deleteDepartmentByIds(Long[] ids);

    /**
     * 删除部门信息
     * 
     * @param id 部门主键
     * @return 结果
     */
    public int deleteDepartmentById(Long id);

    /**
     * 查询启用部门列表
     *
     * @return 启用部门列表
     */
    public List<DepartmentVO> selectDepartmentVOList();
}
