package com.bosch.masterdata.service;

import java.util.List;
import com.bosch.masterdata.domain.SupplierInfo;
import com.bosch.masterdata.domain.dto.SupplierInfoDTO;
import com.bosch.masterdata.domain.vo.SupplierInfoVO;

/**
 * 供应商Service接口
 * 
 * @author xuhao
 * @date 2022-09-22
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

    public List<SupplierInfo> selectSupplierInfoList(SupplierInfoDTO supplierInfoDTO);
    /**
     * 新增供应商
     * 
     * @param supplierInfo 供应商
     * @return 结果
     */
    public int insertSupplierInfo(SupplierInfo supplierInfo);

    public int insertSupplierInfo(SupplierInfoDTO supplierInfoDTO);

    /**
     * 修改供应商
     * 
     * @param supplierInfo 供应商
     * @return 结果
     */
    public int updateSupplierInfo(SupplierInfo supplierInfo);

    public int updateSupplierInfo(SupplierInfoDTO supplierInfoDTO);
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
