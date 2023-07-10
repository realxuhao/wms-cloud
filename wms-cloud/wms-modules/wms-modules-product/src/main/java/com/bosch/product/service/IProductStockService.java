package com.bosch.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.binin.api.domain.dto.IQCChangeStatusDTO;
import com.bosch.binin.api.domain.dto.ManualBinInDTO;
import com.bosch.masterdata.api.domain.vo.AreaVO;
import com.bosch.product.api.domain.ProductReceive;
import com.bosch.product.api.domain.ProductStock;
import com.bosch.product.api.domain.ProductWareShift;
import com.bosch.product.api.domain.dto.*;
import com.bosch.product.api.domain.vo.ProductReturnVO;
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

    List<ProductStockVO> allList(ProductStockQueryDTO stockQueryDTO);



    AreaVO getAreaByType(String wareCode, Integer areaType);

    ProductStock binIn(ProductBinInDTO binInDTO);

    List<ProductStockVO> selectIQCManagementList(ProductIQCManagementQueryDTO queryDTO);

    boolean validateStatus(Long id);

    Integer changeStatus(IQCChangeStatusDTO iqcChangeStatusDTO);

    ProductStock binInToArea(ProductBinInDTO binInDTO);

    void editStock(EditStockDTO dto);

    List<ProductStockVO> selectSUQAIQCManagementList(ProductIQCManagementQueryDTO queryDTO);

    boolean validateSUQAStatus(Long id);

    Integer changeSUQAStatus(IQCChangeStatusDTO iqcChangeStatusDTO);

    List<ProductStockVO> spdnStocklist(ProductStockQueryDTO stockQueryDTO);

    void adjustStock(ProductStockEditDTO stockEditDTO);

    void stockReturn(ProductReturnDTO productReturnDTO);

    void trans(ManualBinInDTO binInDTO);

    List<ProductReturnVO> returnList(ProductReturnDTO queryDTO);
}
