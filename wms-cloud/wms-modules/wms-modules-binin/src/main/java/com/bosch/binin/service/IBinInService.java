package com.bosch.binin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.binin.api.domain.BinIn;
import com.bosch.binin.api.domain.dto.BinAllocationDTO;
import com.bosch.binin.api.domain.dto.BinInDTO;
import com.bosch.binin.api.domain.dto.BinInQueryDTO;
import com.bosch.binin.api.domain.dto.BinInTaskDTO;
import com.bosch.binin.api.domain.vo.BinAllocationVO;
import com.bosch.binin.api.domain.vo.BinInVO;

import java.util.List;

/**
 * @author: UWH4SZH
 * @since: 10/19/2022 10:03
 * @description:
 */
public interface IBinInService extends IService<BinIn> {

    List<BinInVO> selectBinVOList(BinInQueryDTO queryDTO);

    String virtualPalletCode(String palletType);

    BinInVO getByMesBarCode(String mesBarCode);

    BinInVO generateInTask(BinInTaskDTO binInTaskDTO);

    List<BinInVO> currentUserData(BinInQueryDTO queryDTO);

    /**
     * 根据历史库存信息，生成新的上架任务（数量变化）
     * @param ssccNumber
     * @param quantity
     * @return
     */
    BinInVO generateInTask(String ssccNumber,Double quantity);

    BinInVO performBinIn(BinInDTO binInDTO);

    int deleteBinInById(Long id);

    List<BinInVO> selectProcessingBinVOList(BinInQueryDTO binInQueryDTO);

    void binDown(String ssccNumber);
}
