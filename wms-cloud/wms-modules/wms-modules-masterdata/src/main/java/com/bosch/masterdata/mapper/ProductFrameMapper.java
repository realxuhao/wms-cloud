package com.bosch.masterdata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.masterdata.api.domain.ProductFrame;
import com.bosch.masterdata.api.domain.SupplierInfo;
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
}
