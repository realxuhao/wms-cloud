package com.bosch.binin.service;

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

    List<ManualTransferOrderVO> list(ManualTransQueryDTO queryDTO);

    Boolean add(AddManualTransDTO dto);

    BinInVO generateBinInJob(String mesBarCode, String wareCode);

    void issueJob(String[] ssccNumbers);

    void cancel(String[] ids);

    void binDown(String mesBarCode);


    BinInVO performBinIn(ManualBinInDTO binInDTO);

    ManualTransferOrder info(String mesBarCode);
}
