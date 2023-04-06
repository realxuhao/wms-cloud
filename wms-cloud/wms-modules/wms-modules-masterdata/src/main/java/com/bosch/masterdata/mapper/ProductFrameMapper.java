package com.bosch.masterdata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.masterdata.api.domain.MaterialBin;
import com.bosch.masterdata.api.domain.ProductFrame;
import com.bosch.masterdata.api.domain.SupplierInfo;
import com.bosch.masterdata.api.domain.dto.ProductFrameDTO;
import com.bosch.masterdata.api.domain.vo.ProductFrameVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-04-03 13:36
 **/
@Mapper
public interface ProductFrameMapper extends BaseMapper<ProductFrame> {
    List<ProductFrameVO> getBinInRule(@Param("materialCode") String materialCode,
                                      @Param("wareCode") String wareCode);

    /**
     * 新增物料库位分配策略
     *
     * @param
     * @return 结果
     */
    public int insertProductFrame(ProductFrame productFrame);


    /**
     * 查询产品框架列表
     *
     * @param productFrameDTO 产品框架
     * @return 产品框架集合
     */
    public List<ProductFrameVO> selectProductFrameList(ProductFrameDTO productFrameDTO);

    /**
     * 修改产品框架
     *
     * @param productFrame 产品框架
     * @return 结果
     */
    public int updateProductFrame(ProductFrame productFrame);

    /**
     * 批量删除产品框架
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteProductFrameByIds(Long[] ids);
}
