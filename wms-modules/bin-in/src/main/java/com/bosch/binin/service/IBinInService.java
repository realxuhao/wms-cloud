package com.bosch.binin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.binin.domain.BinIn;
import com.bosch.binin.domain.dto.BinAllocationDTO;
import com.bosch.binin.domain.dto.BinInQueryDTO;
import com.bosch.binin.domain.vo.BinAllocationVO;
import com.bosch.binin.domain.vo.BinInVO;
import com.bosch.masterdata.api.domain.Area;

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
