package com.bosch.maindata.service.impl;

import java.util.List;
import com.ruoyi.common.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bosch.maindata.mapper.SupplierInfoMapper;
import com.bosch.maindata.domain.SupplierInfo;
import com.bosch.maindata.service.ISupplierInfoService;

/**
 * 供应商Service业务层处理
 * 
 * @author xuhao
 * @date 2022-09-21
 */
@Service
public class SupplierInfoServiceImpl implements ISupplierInfoService 
{
    @Autowired
    private SupplierInfoMapper supplierInfoMapper;

    /**
     * 查询供应商
     * 
     * @param id 供应商主键
     * @return 供应商
     */
    @Override
    public SupplierInfo selectSupplierInfoById(Long id)
    {
        return supplierInfoMapper.selectSupplierInfoById(id);
    }

    /**
     * 查询供应商列表
     * 
     * @param supplierInfo 供应商
     * @return 供应商
     */
    @Override
    public List<SupplierInfo> selectSupplierInfoList(SupplierInfo supplierInfo)
    {
        return supplierInfoMapper.selectSupplierInfoList(supplierInfo);
    }

    /**
     * 新增供应商
     * 
     * @param supplierInfo 供应商
     * @return 结果
     */
    @Override
    public int insertSupplierInfo(SupplierInfo supplierInfo)
    {
        supplierInfo.setCreateTime(DateUtils.getNowDate());
        return supplierInfoMapper.insertSupplierInfo(supplierInfo);
    }

    /**
     * 修改供应商
     * 
     * @param supplierInfo 供应商
     * @return 结果
     */
    @Override
    public int updateSupplierInfo(SupplierInfo supplierInfo)
    {
        supplierInfo.setUpdateTime(DateUtils.getNowDate());
        return supplierInfoMapper.updateSupplierInfo(supplierInfo);
    }

    /**
     * 批量删除供应商
     * 
     * @param ids 需要删除的供应商主键
     * @return 结果
     */
    @Override
    public int deleteSupplierInfoByIds(Long[] ids)
    {
        return supplierInfoMapper.deleteSupplierInfoByIds(ids);
    }

    /**
     * 删除供应商信息
     * 
     * @param id 供应商主键
     * @return 结果
     */
    @Override
    public int deleteSupplierInfoById(Long id)
    {
        return supplierInfoMapper.deleteSupplierInfoById(id);
    }
}
