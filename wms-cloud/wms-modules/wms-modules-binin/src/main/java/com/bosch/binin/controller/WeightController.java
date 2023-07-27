package com.bosch.binin.controller;

import com.bosch.binin.api.domain.dto.AddShiftTaskDTO;
import com.bosch.binin.api.domain.dto.WeightDTO;
import com.bosch.binin.service.IWeightService;
import com.bosch.storagein.api.domain.Weight;
import com.bosch.storagein.api.domain.vo.WeightVO;
import com.ruoyi.common.core.domain.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @GetMapping(value = "/getByIp/{ip}")
    public R<Weight> getByPort(@PathVariable("ip") String ip) {

        return R.ok(weightService.getByPort(ip));
    }


    @GetMapping(value = "/getWeightList")
    public R<List<WeightVO>> getWeightIpList() {
        WeightVO weightVO1 = new WeightVO();
        WeightVO weightVO2 = new WeightVO();

        WeightVO weightVO3 = new WeightVO();
        WeightVO weightVO4 = new WeightVO();

        weightVO1.setCode("001");
        weightVO1.setIp("10.200.172.193");

        weightVO2.setCode("002");
        weightVO2.setIp("10.200.172.194");


        weightVO3.setCode("003");
        weightVO3.setIp("10.200.230.19");

        weightVO4.setCode("004");
        weightVO4.setIp("10.200.229.19");

        List<WeightVO> arrayList = new ArrayList<>();
        arrayList.add(weightVO1);
        arrayList.add(weightVO2);
        arrayList.add(weightVO3);
        arrayList.add(weightVO4);

        return R.ok(arrayList);
    }

}
