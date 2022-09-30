package com.bosch.masterdata.service.impl;

import java.util.List;

import com.bosch.masterdata.api.domain.dto.SupplierInfoDTO;
import com.bosch.masterdata.utils.BeanConverUtil;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bosch.masterdata.mapper.SupplierInfoMapper;
import com.bosch.masterdata.api.domain.SupplierInfo;
import com.bosch.masterdata.service.ISupplierInfoService;

/**
 * 供应商Service业务层处理
 * 
 * @author xuhao
 * @date 2022-09-22
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

    @Override
    public List<SupplierInfo> selectSupplierInfoList(SupplierInfoDTO supplierInfoDTO) {
        SupplierInfo supplierInfo= BeanConverUtil.conver(supplierInfoDTO,SupplierInfo.class);
//        List<SupplierInfoVO> supplierInfoVOS =
//                BeanConverUtil.converList(supplierInfoMapper.selectSupplierInfoList(supplierInfo),
//                        SupplierInfoVO.class);

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

    @Override
    public int insertSupplierInfo(SupplierInfoDTO supplierInfoDTO) {
        SupplierInfo supplierInfo = BeanConverUtil.conver(supplierInfoDTO, SupplierInfo.class);
        supplierInfo.setCreateTime(DateUtils.getNowDate());
        supplierInfo.setCreateBy(SecurityUtils.getUsername());
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

    @Override
    public int updateSupplierInfo(SupplierInfoDTO supplierInfoDTO) {
        SupplierInfo supplierInfo = BeanConverUtil.conver(supplierInfoDTO, SupplierInfo.class);
        supplierInfo.setUpdateTime(DateUtils.getNowDate());
        supplierInfo.setUpdateBy(SecurityUtils.getUsername());
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
