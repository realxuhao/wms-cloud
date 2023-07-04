package com.bosch.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.binin.api.domain.ManualTransferOrder;
import com.bosch.binin.api.domain.dto.AddManualTransDTO;
import com.bosch.binin.api.domain.dto.ManualBinInDTO;
import com.bosch.binin.api.domain.dto.ManualTransQueryDTO;
import com.bosch.binin.api.domain.vo.BinInVO;
import com.bosch.binin.api.domain.vo.ManualTransferOrderVO;

import java.util.List;

/**
 * @program: wms-cloud
 * @description: 手工创建转储单service
 * @author: xuhao
 * @create: 2022-12-20 13:10
 **/
public interface IManualTransferOrderService extends IService<ManualTransferOrder> {


}
