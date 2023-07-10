package com.bosch.product.api.domain.vo;

import com.bosch.product.api.domain.ProductReturn;
import com.ruoyi.common.core.annotation.Excel;
import lombok.Data;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-07-10 15:14
 **/
@Data
public class ProductReturnVO extends ProductReturn {

    @Excel(name = "物料名称")
    private String materialName;
}
