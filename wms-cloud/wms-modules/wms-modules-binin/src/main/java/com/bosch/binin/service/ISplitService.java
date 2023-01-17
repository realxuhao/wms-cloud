package com.bosch.binin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.binin.api.domain.SplitRecord;
import com.bosch.binin.api.domain.dto.SplitPalletDTO;
import com.bosch.binin.api.domain.dto.SplitQuertDTO;
import com.bosch.binin.api.domain.vo.SplitRecordVO;

import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-01-17 09:54
 **/
public interface ISplitService extends IService<SplitRecord> {

    void add(SplitPalletDTO splitPallet);

    List<SplitRecordVO> getList(SplitQuertDTO queryDTO);
}
