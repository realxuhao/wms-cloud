package com.bosch.binin.controller;

import com.bosch.binin.api.domain.dto.AddShiftTaskDTO;
import com.bosch.binin.api.domain.dto.WeightDTO;
import com.ruoyi.common.core.domain.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


    @PostMapping(value = "/add")
    @ApiOperation("新增称重数据")
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> add(@RequestBody WeightDTO dto) {

        return R.ok();
    }
}
