package com.bosch.binin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.storagein.api.domain.BinIn;
import com.bosch.storagein.api.domain.dto.BinAllocationDTO;
import com.bosch.storagein.api.domain.dto.BinInQueryDTO;
import com.bosch.storagein.api.domain.vo.BinAllocationVO;
import com.bosch.storagein.api.domain.vo.BinInVO;

import java.util.List;

/**
 * @author: UWH4SZH
 * @since: 10/19/2022 10:03
 * @description:
 */
public interface IBinInService extends IService<BinIn> {

    List<BinInVO> selectBinVOList(BinInQueryDTO queryDTO);

    String virtualPalletCode(String palletType);

    BinAllocationVO allocateBinCode(BinAllocationDTO binAllocationDTO);
}
