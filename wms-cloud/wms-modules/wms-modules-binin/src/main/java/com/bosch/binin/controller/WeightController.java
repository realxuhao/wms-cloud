package com.bosch.binin.controller;

import com.bosch.binin.api.domain.dto.AddShiftTaskDTO;
import com.bosch.binin.api.domain.dto.WeightDTO;
import com.bosch.binin.service.IWeightService;
import com.bosch.storagein.api.domain.Weight;
import com.ruoyi.common.core.domain.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLOutput;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-02-14 10:53
 **/
@RestController
@Api(tags = "称重接口")
@RequestMapping("/weight")
public class WeightController {


    @Autowired
    private IWeightService weightService;


    @PostMapping(value = "/add")
    @ApiOperation("新增称重数据")
    public R add(@RequestBody WeightDTO dto) {
        weightService.addWeight(dto);
        return R.ok();
    }

    @GetMapping(value = "/getByPort")
    public R<Weight> getByPort(@RequestParam("ip") String ip,
                               @RequestParam("port") Integer port) {

        return R.ok(weightService.getByPort(ip,port));
    }
}
