package com.bosch.binin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.binin.api.domain.BinIn;
import com.bosch.binin.api.domain.MaterialKanban;
import com.bosch.binin.api.domain.Stock;
import com.bosch.binin.api.domain.dto.MaterialKanbanDTO;
import com.bosch.binin.api.domain.vo.MaterialKanbanVO;
import com.bosch.binin.api.domain.vo.StockVO;
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

    void issueJob(Long[] ids);

    List<MaterialKanbanVO> getWaitingJob(String mesbarCode);

    boolean updateStocks (List<MaterialKanban> list);

}
