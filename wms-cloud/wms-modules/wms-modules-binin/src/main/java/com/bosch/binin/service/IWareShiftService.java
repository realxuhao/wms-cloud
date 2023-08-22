package com.bosch.binin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.binin.api.domain.BinIn;
import com.bosch.binin.api.domain.WareShift;
import com.bosch.binin.api.domain.dto.*;
import com.bosch.binin.api.domain.vo.BinInVO;
import com.bosch.binin.api.domain.vo.WareShiftVO;

import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-11-16 20:48
 **/
public interface IWareShiftService extends IService<WareShift> {
    Boolean addShiftRequirement(AddShiftTaskDTO dto);

    WareShift binDown(String mesBarCode);

    /**
     *
     * @param ssccs  sscc集合
     * @param queryStatus 查询状态
     * @param status 更新状态
     * @return
     */
    int updateStatusByStatus(List<String> ssccs, Integer queryStatus, Integer status);

    BinInVO allocateBin(String mesBarCode,String wareCode);

    List<WareShiftVO> getWareShiftList(WareShiftQueryDTO queryDTO);

    List<WareShiftVO> getWaitingBinIn();

    WareShift cancelWareShift(Long id);

    WareShift getWareShiftBySsccAndStatus(String sscc);

    List<WareShift> getListBySSCC(List<String> ssccs);

    Boolean add(AddShiftTaskDTO dto);

    BinInVO performBinIn(BinInDTO binInDTO);

    /**
     * 计算某个物料的在途量
     * @param materialNb
     * @return
     */
    Double getInTransitCount(String materialNb);

    void generateWareShiftByCall(List<CallWareShiftDTO> dtos);

    void splitPallet(SplitPalletDTO splitPallet);

    List<BinIn> batchPerformBinIn(WareShiftBatchBinInDTO dto);
}
