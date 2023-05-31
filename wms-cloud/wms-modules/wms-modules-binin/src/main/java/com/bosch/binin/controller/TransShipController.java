package com.bosch.binin.controller;

import com.bosch.binin.api.domain.TranshipmentOrder;
import com.bosch.binin.service.ITranshipmentOrderService;
import com.ruoyi.common.core.domain.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-03-30 09:41
 **/
@RestController
@Api(tags = "发运接口")
@RequestMapping("/trans-ship")
public class TransShipController {

    @Autowired
    private ITranshipmentOrderService transhipmentOrderService;


    @GetMapping(value = "/getNextOrderNb")
    @ApiOperation("库存列表")
    public R<String> list() {
        return R.ok(transhipmentOrderService.getNextOrderNb());
    }

    @PostMapping("/saveBatch")
    public R saveBatch(@RequestBody List<TranshipmentOrder> transhipmentOrderList){
        transhipmentOrderService.saveBatch(transhipmentOrderList);
        return R.ok();
    }


    @GetMapping("/getTransInfo/{mesBarCode}")
    @ApiOperation("扫码查询当前车次信息")
    public R<TranshipmentOrder> getTransInfo(@PathVariable String mesBarCode){
        return R.ok(transhipmentOrderService.getTransInfo(mesBarCode));
    }

}
