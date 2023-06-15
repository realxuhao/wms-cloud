package com.bosch.binin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.binin.api.domain.Stock;
import com.bosch.binin.api.domain.dto.*;
import com.bosch.binin.api.domain.vo.BinInVO;
import com.bosch.binin.api.domain.vo.StockVO;
import com.bosch.masterdata.api.domain.dto.IQCDTO;
import com.bosch.masterdata.api.domain.vo.IQCVO;

import java.util.List;

/**
 * @author: UWH4SZH
 * @since: 10/19/2022 15:56
 * @description:
 */
public interface IStockService extends IService<Stock> {

    List<StockVO> selectStockVOList(StockQueryDTO stockQuerySTO);

    List<StockVO> selectStockVOListBySSCC(List<String> ssccs);



    List<StockVO> selectIQCManagementList(IQCManagementQueryDTO iqcManagementQueryDTO);

    boolean validateStatus(Long id);

    Integer changeStatus(IQCChangeStatusDTO iqcChangeStatusDTO);

    List<IQCVO> excelChangeStatus(List<IQCDTO> list);

    List<IQCVO> mapToMaterial(List<IQCVO> result);
    List<IQCDTO> iqcDTOSToMaterial(List<IQCDTO> result);
    List<StockVO> selectStockVOBySortType(StockQueryDTO stockQuerySTO);

    Double countAvailableStock(StockQueryDTO stockQueryDTO);

    StockVO getLastOneBySSCC(String ssccs);

    Stock getAvailablesStockBySscc(String sscc);

    /**
     * 获取一个库存信息，主要是为了获取批次、过期时间时间，比如：拆托后，需要获得同样批次、BBD等。
     * @param sscc
     * @return
     */
    Stock getOneStock(String sscc);

    List<StockVO> getBinStockLog(String binCode);

    /**
     * 主库库存量
     * @param materialNb
     * @return
     */
    Double getMainStockCount(String materialNb);


    /**
     * 暂时不可用的可用量
     * @param materialNb
     * @return
     */
    Double getNoAvailableStockCount(String materialNb);

    /**
     * 外库库存量
     */
    Double getOutStockCount(String materialNb);


    void initStock(List<InitStockDTO> list);

    void editStock(StockEditDTO stockEditDTO);
}
