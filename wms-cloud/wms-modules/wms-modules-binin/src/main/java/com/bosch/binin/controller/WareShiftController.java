package com.bosch.binin.controller;

import com.bosch.binin.api.domain.dto.AddShiftTaskDTO;
import com.bosch.binin.service.IMaterialShiftService;
import com.ruoyi.common.core.domain.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-11-16 21:15
 **/
@RestController
@Api(tags = "移库")
@RequestMapping("/ware-shift")
public class WareShiftController {

    @Autowired
    private IMaterialShiftService shiftService;

    @PostMapping(value = "/addShiftTask")
    @ApiOperation("外库新增需求接口")
    public R<String> list(@RequestBody AddShiftTaskDTO dto) {
        shiftService.addShiftRequirement(dto);
        return null;
    }


}
