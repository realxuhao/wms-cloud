package com.bosch.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.binin.api.domain.dto.WareShiftQueryDTO;
import com.bosch.product.api.domain.ProductWareShift;
import com.bosch.product.api.domain.dto.ProductBinInDTO;
import com.bosch.product.api.domain.vo.ProductWareShiftVO;

import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-03-29 16:51
 **/
public interface IProductWareShiftService extends IService<ProductWareShift> {
    void addByStockId(Long stockId);

    void cancel(Long id);

    void ship(List<String> ssccList,String carNb);

    List<ProductWareShiftVO> list(WareShiftQueryDTO queryDTO);

    void receive(String qrCode);

    void wareShiftBinIn(ProductBinInDTO binInDTO);
}