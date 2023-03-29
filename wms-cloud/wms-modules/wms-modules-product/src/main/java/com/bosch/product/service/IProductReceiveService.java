package com.bosch.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.product.api.domain.ProductReceive;
import com.bosch.product.api.domain.dto.ProductReceiveQueryDTO;
import com.bosch.product.api.domain.vo.ProductReceiveVO;

import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-03-29 11:20
 **/
public interface IProductReceiveService extends IService<ProductReceive> {


    List<ProductReceiveVO> list(ProductReceiveQueryDTO queryDTO);

    void receive(String qrCode);

    void delete(Long id);
}
