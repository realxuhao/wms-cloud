package com.bosch.binin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.binin.api.domain.BinIn;
import com.bosch.binin.api.domain.dto.*;
import com.bosch.binin.api.domain.vo.BinAllocationVO;
import com.bosch.binin.api.domain.vo.BinInVO;
import com.bosch.masterdata.api.domain.Ware;
import com.bosch.masterdata.api.domain.vo.BinVO;
import com.bosch.masterdata.api.domain.vo.MaterialVO;

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

    BinInVO allocateToBin(String mesBarCode,Double quantity);

    BinInVO generateInTaskByMesBarCode(String mesBarCode,Double quantity);

    /**
     * 分配到具体的区域或者库位，如果是分配到库位，那么areaCode为null
     *
     * @param ssccNb
     * @param materialCode
     * @param binCode
     * @param areaCode
     * @return
     */
    BinInVO allocateToBinOrArea(String ssccNb, String materialCode, String binCode, String areaCode, Double quantity);


    BinInVO performToAreaType(String mesBarCode,Double quantity, Integer areaType);

    BinInVO allocateToBinOrArea(String mesBarCode,Double quantity, String binCode, String areaCode);


    List<BinInVO> currentUserData(BinInQueryDTO queryDTO);

    /**
     * 根据历史库存信息，生成新的上架任务（数量变化）
     *
     * @param ssccNumber
     * @param quantity
     * @return
     */
    BinInVO generateInTaskByOldStock(String ssccNumber, Double quantity, String wareCode);

    BinInVO performBinIn(BinInDTO binInDTO,String quantityStatus);

    /**
     * 原材料上架 ，走IQC
     * @param binInDTO
     * @return
     */
    public BinInVO performBinInWithIQC(BinInDTO binInDTO);

    int deleteBinInBySscc(String ssccNumber);

    List<BinInVO> selectProcessingBinVOList(BinInQueryDTO binInQueryDTO);

    void binDown(String ssccNumber);

    public BinVO getBinVOByBinCode(String binCode);


    public MaterialVO getMaterialVOByCode(String materialNb);

    void batchBinIn(String mesBarCode, String areaCode);


    public Ware getWareInfo(String wareCode);
}
