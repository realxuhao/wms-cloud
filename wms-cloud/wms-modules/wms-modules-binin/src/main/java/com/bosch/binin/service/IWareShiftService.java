package com.bosch.binin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.binin.api.domain.WareShift;
import com.bosch.binin.api.domain.dto.AddShiftTaskDTO;

import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-11-16 20:48
 **/
public interface IWareShiftService extends IService<WareShift> {
    Boolean addShiftRequirement(AddShiftTaskDTO dto);

    void binDown(String ssccNb);

    /**
     *
     * @param ssccs  sscc集合
     * @param queryStatus 查询状态
     * @param status 更新状态
     * @return
     */
    int updateStatusByStatus(List<String> ssccs, Integer queryStatus, Integer status);
}
