package com.bosch.binin.service;

import com.bosch.binin.api.domain.vo.JobVO;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-08-09 10:07
 **/
public interface IJobService {

    void validStockStatus(String ssccNb);


    JobVO getJobDescBySSCC(String ssccNb);


    void validStockStatusWithoutIQC(String ssccNb);



}
