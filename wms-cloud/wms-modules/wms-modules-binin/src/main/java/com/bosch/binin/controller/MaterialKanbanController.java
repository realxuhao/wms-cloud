package com.bosch.binin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bosch.binin.api.domain.dto.MaterialKanbanDTO;
import com.bosch.binin.api.domain.vo.MaterialKanbanVO;
import com.bosch.binin.api.domain.vo.StockVO;
import com.bosch.binin.service.IMaterialKanbanService;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Api(tags = "kanban接口")
@RequestMapping("/materialKanban")
public class MaterialKanbanController {

    @Autowired
    private IMaterialKanbanService materialKanbanService;


    @PostMapping(value = "/list")
    @ApiOperation("查询kanban列表")
    public R<PageVO<MaterialKanbanVO>> list(@RequestBody MaterialKanbanDTO materialKanbanDTO) {

        IPage<MaterialKanbanVO> materialKanbanIPage = materialKanbanService.pageList(materialKanbanDTO);

        return R.ok(new PageVO<>(materialKanbanIPage.getRecords(), new PageInfo<>(materialKanbanIPage.getRecords()).getTotal()));
    }


    @GetMapping(value = "/getStockInfo")
    @ApiOperation("根据仓库code和物料code查询kanban任务中对应的库存信息")
    public R<List<StockVO>> getStockInfo(@RequestParam("materialNb") String materialNb,
                                         @RequestParam("wareCode") String wareCode) {
        List<StockVO> stockVOS = materialKanbanService.getStockInfo(materialNb, wareCode);
        return R.ok(stockVOS);
    }
}
