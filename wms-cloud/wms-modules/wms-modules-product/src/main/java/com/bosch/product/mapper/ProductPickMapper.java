package com.bosch.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.product.api.domain.ProductPick;
import com.bosch.product.api.domain.ProductReceive;
import com.bosch.product.api.domain.dto.ProductPickDTO;
import com.bosch.product.api.domain.dto.ProductReceiveDTO;
import com.bosch.product.api.domain.dto.ProductReceiveQueryDTO;
import com.bosch.product.api.domain.dto.SUDNDTO;
import com.bosch.product.api.domain.vo.ProductPickExportVO;
import com.bosch.product.api.domain.vo.ProductPickVO;
import com.bosch.product.api.domain.vo.ProductReceiveVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-03-29 13:05
 **/
@Mapper
public interface ProductPickMapper extends BaseMapper<ProductPick> {

    List<ProductPickVO> list(ProductPickDTO queryDTO);


    List<ProductPickVO> binDownlist(ProductPickDTO queryDTO);

    List<ProductPickExportVO> getSUDNPickExportVO(ProductPickDTO sudndto);
}
