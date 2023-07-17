package com.bosch.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.masterdata.api.domain.dto.MdProductPackagingDTO;
import com.bosch.product.api.domain.ProductReceive;
import com.bosch.product.api.domain.dto.ProductReceiveDTO;
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

    void receive(String qrCode,Double quantity);

    void delete(Long id);


    ProductReceive getProductReceiveVO(String sscc);

    /**
     * 查询信息是否重复
     *
     * @param dtos
     * @return
     */
    boolean validList(List<ProductReceiveDTO> dtos);
}
