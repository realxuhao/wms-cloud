package com.bosch.storagein.controller;

import com.bosch.storagein.api.constants.ResponseConstants;
import com.bosch.storagein.api.domain.Weight;
import com.bosch.storagein.api.domain.dto.MaterialInCheckDTO;
import com.bosch.storagein.api.domain.vo.MaterialCheckResultVO;
import com.bosch.storagein.api.domain.vo.MaterialInVO;
import com.bosch.storagein.service.IWeightService;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Objects;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-02-17 10:31
 **/
@RestController
@Api(tags = "原材料收货接口")
@RequestMapping("/weight")
public class WeightController {

    @Autowired
    private IWeightService weightService;


    /**
     * 添加称重数据
     */
    @PostMapping(value = "/add")
    @Log(title = "添加称重数据", businessType = BusinessType.INSERT)
    @ApiOperation("添加称重数据")
    public R add(@RequestBody Weight weight) {
        if (Objects.isNull(weight)) {
            throw new ServiceException("称重数据不能为空");
        }
        weightService.uploadWeight(weight);
        return R.ok();
    }

    @GetMapping(value = "/getByPort/{port}")
    @ApiOperation("根据端口号获取称重数据")
    public AjaxResult getInfo(@PathVariable("port") Integer port) {
        return AjaxResult.success(weightService.getByPort(port));
    }

    @GetMapping(value = "/getByIp/{ip}")
    @ApiOperation("根据端口号获取称重数据")
    public AjaxResult getInfo(@PathVariable("ip") String ip) {
        return AjaxResult.success(weightService.getWeightByIP(ip));
    }


}
