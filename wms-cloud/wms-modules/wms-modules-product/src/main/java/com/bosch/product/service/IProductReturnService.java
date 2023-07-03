package com.bosch.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.product.api.domain.ProductReturn;
import com.bosch.product.api.domain.dto.ProductReturnDTO;
import org.springframework.stereotype.Service;

/**
* @author GUZ1CGD4
* @description 针对表【material_return(退库表)】的数据库操作Service
* @createDate 2022-12-12 11:09:13
*/
@Service
public interface IProductReturnService extends IService<ProductReturn> {


}
