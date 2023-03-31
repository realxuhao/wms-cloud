package com.bosch.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.binin.api.domain.dto.IQCChangeStatusDTO;
import com.bosch.binin.api.domain.dto.IQCManagementQueryDTO;
import com.bosch.masterdata.api.domain.vo.AreaVO;
import com.bosch.product.api.domain.ProductReceive;
import com.bosch.product.api.domain.ProductStock;
import com.bosch.product.api.domain.ProductWareShift;
import com.bosch.product.api.domain.dto.ProductBinInDTO;
import com.bosch.product.api.domain.dto.ProductIQCManagementQueryDTO;
import com.bosch.product.api.domain.dto.ProductStockQueryDTO;
import com.bosch.product.api.domain.vo.ProductReceiveVO;
import com.bosch.product.api.domain.vo.ProductStockVO;

import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-03-29 11:21
 **/
public interface IProductStockService extends IService<ProductStock> {

    /**
     * 收货生成库存
     *
     * @param receive
     */
    void generateStockByReceive(ProductReceive receive);


    /**
     * 移库批量生成库存
     *
     * @param productWareShiftList
     */
    void generateStockByProductWareShifts(List<ProductWareShift> productWareShiftList);


    List<ProductStockVO> list(ProductStockQueryDTO stockQueryDTO);


    AreaVO getAreaByType(String wareCode, Integer areaType);

    ProductStock binIn(ProductBinInDTO binInDTO);

    List<ProductStockVO> selectIQCManagementList(ProductIQCManagementQueryDTO queryDTO);

    boolean validateStatus(Long id);

    Integer changeStatus(IQCChangeStatusDTO iqcChangeStatusDTO);
}
