package com.bosch.product.controller;

import com.bosch.masterdata.api.domain.vo.PageVO;
import com.bosch.product.service.IProductWareShiftService;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-03-29 16:26
 **/
@RestController
@Api(tags = "成品移库接口")
@RequestMapping("/product-shift")
public class ProductWareShiftController {

    @Autowired
    private IProductWareShiftService wareShiftService;


    @PutMapping(value = "/generateShift/{stockId}")
    @ApiOperation("生成移库任务")
    public R add(@PathVariable("stockId") Long stockId) {
        wareShiftService.addByStockId(stockId);
        return R.ok();
    }

    @PutMapping(value = "/cancel/{id}")
    @ApiOperation("取消移库任务")
    public R cancel(@PathVariable("id") Long id){
        wareShiftService.cancel(id);
        return R.ok();
    }


    @PutMapping(value = "/ship/{ssccList}")
    @ApiOperation("发运")
    public R ship(@PathVariable("ssccList") List<String> ssccList){
        wareShiftService.ship(ssccList);
        return R.ok();
    }




}
