package com.bosch.maindata.service;

import java.util.List;
import com.bosch.maindata.domain.SupplierInfo;

/**
 * 供应商Service接口
 * 
 * @author xuhao
 * @date 2022-09-21
 */
public interface ISupplierInfoService 
{
    /**
     * 查询供应商
     * 
     * @param id 供应商主键
     * @return 供应商
     */
    public SupplierInfo selectSupplierInfoById(Long id);

    /**
     * 查询供应商列表
     * 
     * @param supplierInfo 供应商
     * @return 供应商集合
     */
    public List<SupplierInfo> selectSupplierInfoList(SupplierInfo supplierInfo);

    /**
     * 新增供应商
     * 
     * @param supplierInfo 供应商
     * @return 结果
     */
    public int insertSupplierInfo(SupplierInfo supplierInfo);

    /**
     * 修改供应商
     * 
     * @param supplierInfo 供应商
     * @return 结果
     */
    public int updateSupplierInfo(SupplierInfo supplierInfo);

    /**
     * 批量删除供应商
     * 
     * @param ids 需要删除的供应商主键集合
     * @return 结果
     */
    public int deleteSupplierInfoByIds(Long[] ids);

    /**
     * 删除供应商信息
     * 
     * @param id 供应商主键
     * @return 结果
     */
    public int deleteSupplierInfoById(Long id);
}
