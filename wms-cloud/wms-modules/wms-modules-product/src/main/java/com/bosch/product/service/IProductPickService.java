package com.bosch.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.product.api.domain.ProductPick;
import com.bosch.product.api.domain.ProductReceive;
import com.bosch.product.api.domain.dto.EditBinDownQuantityDTO;
import com.bosch.product.api.domain.dto.ProductPickDTO;
import com.bosch.product.api.domain.dto.ProductReceiveDTO;
import com.bosch.product.api.domain.dto.ProductReceiveQueryDTO;
import com.bosch.product.api.domain.vo.ProductPickBinDownVO;
import com.bosch.product.api.domain.vo.ProductPickVO;
import com.bosch.product.api.domain.vo.ProductReceiveVO;

import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-03-29 11:20
 **/
public interface IProductPickService extends IService<ProductPick> {


    List<ProductPickVO> list(ProductPickDTO queryDTO);

    void batchCancel(List<Long> idList);

    void cancel(Long id);

    void modifySscc(Long id,Long stockId);

    ProductPickBinDownVO binDown(String qrCode, Long sudnId);

    List<ProductPickVO> getBySudnId(Long sudnId,Integer status);


    void editBinDownQuantity(EditBinDownQuantityDTO dto);

    void batchIssue(List<Long> idList);
}
