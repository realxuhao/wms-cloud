package com.bosch.masterdata.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.masterdata.api.domain.MaterialBin;
import com.bosch.masterdata.api.domain.SupplierInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 供应商Mapper接口
 * 
 * @author xuhao
 * @date 2022-09-22
 */
@Mapper
@Repository("supplierInfoMapper")
public interface SupplierInfoMapper extends BaseMapper<SupplierInfo>
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
     * 删除供应商
     * 
     * @param id 供应商主键
     * @return 结果
     */
    public int deleteSupplierInfoById(Long id);

    /**
     * 批量删除供应商
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSupplierInfoByIds(Long[] ids);
}
