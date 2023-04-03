package com.bosch.masterdata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.masterdata.api.domain.ProductFrame;
import com.bosch.masterdata.api.domain.vo.ProductFrameVO;

import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-04-03 13:34
 **/
public interface IProductFrameService extends IService<ProductFrame> {

    List<ProductFrameVO> getBinInRule(String materialCode, String wareCode);
}
