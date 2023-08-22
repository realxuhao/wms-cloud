package com.bosch.binin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.binin.api.domain.BinIn;
import com.bosch.binin.api.domain.MaterialKanban;
import com.bosch.binin.api.domain.Stock;
import com.bosch.binin.api.domain.dto.MaterialKanbanDTO;
import com.bosch.binin.api.domain.dto.SplitPalletDTO;
import com.bosch.binin.api.domain.vo.MaterialInfoVO;
import com.bosch.binin.api.domain.vo.MaterialKanbanVO;
import com.bosch.binin.api.domain.vo.StockVO;
import com.bosch.binin.api.enumeration.KanbanStatusEnum;
import com.ruoyi.common.core.web.page.PageDomain;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IMaterialKanbanService extends IService<MaterialKanban> {

    IPage<MaterialKanbanVO> pageList(MaterialKanbanDTO dto);

    List<StockVO> getStockInfo(String materialNb, String wareCode);

    int deleteByIds(Long[] ids);

    boolean checkStock(List<MaterialKanbanDTO> materialKanbanDTOS);

    List<Stock> selectStockList(List<MaterialKanbanDTO> dtos);

    /**
     * 库存表数据转换看板数据
     * @param stocks
     * @return
     */
    List<MaterialKanban> setValue(List<Stock> stocks,List<MaterialKanbanDTO> dtos);


    /**
     * 更新stock
     * @param sscc
     * @param quantity
     * @return
     */
    int updateStockBySSCC(String sscc,Double quantity);

    /**
     * 更新Kanban取消
     * @param id
     * @return
     */
    int updateKanban(Long id);

    List<MaterialKanban> issueJob(Long[] ids);

    MaterialKanbanVO getWaitingJob(String mesbarCode);

    boolean updateStocks (List<MaterialKanban> list);

    /**
     *
     * @param dto
     * @return
     */
    public List<MaterialKanbanVO> receivingMaterialList(MaterialKanbanDTO dto);

    /**
     *
     * @param dto
     * @return
     */
    public List<MaterialKanbanVO> receivedMaterialList(MaterialKanbanDTO dto);

    /**
     *
     * @param sscc
     * @param wareCode
     * @return
     */
    public List<MaterialInfoVO> materialInfoList(String sscc,String wareCode);

    /**
     *
     * @param ids
     * @param status
     * @return
     */
    int updateKanbanByIds(List<Long> ids,Integer status);

    int updateKanbanBySSCC(List<String> ssccs,Integer status);

    /**
     *
     * @param ssccs  sscc集合
     * @param queryStatus 查询状态
     * @param status 更新状态
     * @return
     */
    int updateKanbanByStatus(List<String> ssccs,Integer queryStatus,Integer status);


    int updateKanbanByIdStatus(List<Long> ids,Integer queryStatus,Integer status);

    /**
     * 根据sscclist获取看板list
     * @param sscc
     * @return
     */
    List<MaterialInfoVO> materialInfoBySSCC(List<String> sscc);

    MaterialKanban binDown(String sscc);

    List<MaterialKanbanVO> binDownList(PageDomain pageDomain, String wareCode);

    List<MaterialKanban> getListBySCAndStatus(List<String> sscc, Integer status);

    List<MaterialKanban> getListByIdAndStatus(List<Long> ids, Integer status);


    MaterialKanban getOneBySCAndStatus(String sscc, Integer status);

    MaterialKanbanVO getKanbanBySSCC(String sscc);

    MaterialKanbanVO getKanbanBySSCCAndStatus(String sscc, KanbanStatusEnum kanbanStatusEnum);
    void splitPallet(SplitPalletDTO splitPallet);

    MaterialKanban cancelKanban(Long id);

    List<MaterialKanbanVO> waitingBinDownList(PageDomain pageDomain, String wareCode);

    List<MaterialKanbanVO> getKanbanList(MaterialKanbanDTO dto);


}
