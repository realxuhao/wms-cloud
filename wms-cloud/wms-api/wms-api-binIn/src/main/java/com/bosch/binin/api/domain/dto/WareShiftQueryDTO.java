package com.bosch.binin.api.domain.dto;

import com.ruoyi.common.core.web.page.PageDomain;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-12-07 14:34
 **/
@Data
public class WareShiftQueryDTO extends PageDomain {

    private String sourceWareCode;
    private String targetWareCode;
    private Integer status;

}
