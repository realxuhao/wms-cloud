package com.bosch.binin.api.domain.dto;

import com.ruoyi.common.core.web.page.PageDomain;
import lombok.Data;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-01-04 10:10
 **/
@Data
public class MaterialReturnQueryDTO extends PageDomain {
    private String wareCode;


    private String areaCode;

    private Integer status;
    private Integer type;
}
