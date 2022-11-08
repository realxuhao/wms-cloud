package com.bosch.masterdata.service;

import java.util.List;
import com.bosch.masterdata.domain.Ware;

/**
 * 仓库Service接口
 * 
 * @author xuhao
 * @date 2022-09-26
 */
public interface IWareService 
{
    /**
     * 查询仓库
     * 
     * @param id 仓库主键
     * @return 仓库
     */
    public Ware selectWareById(String id);

    /**
     * 查询仓库列表
     * 
     * @param ware 仓库
     * @return 仓库集合
     */
    public List<Ware> selectWareList(Ware ware);

    /**
     * 新增仓库
     * 
     * @param ware 仓库
     * @return 结果
     */
    public int insertWare(Ware ware);

    /**
     * 修改仓库
     * 
     * @param ware 仓库
     * @return 结果
     */
    public int updateWare(Ware ware);

    /**
     * 批量删除仓库
     * 
     * @param ids 需要删除的仓库主键集合
     * @return 结果
     */
    public int deleteWareByIds(String[] ids);

    /**
     * 删除仓库信息
     * 
     * @param id 仓库主键
     * @return 结果
     */
    public int deleteWareById(String id);
}