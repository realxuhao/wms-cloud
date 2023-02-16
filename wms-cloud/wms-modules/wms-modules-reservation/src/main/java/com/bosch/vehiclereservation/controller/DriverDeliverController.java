package com.bosch.vehiclereservation.controller;


import com.bosch.vehiclereservation.api.domain.dto.DriverDeliverDTO;
import com.bosch.vehiclereservation.api.domain.vo.DriverDeliverVO;
import com.bosch.vehiclereservation.api.domain.vo.PageVO;
import com.bosch.vehiclereservation.service.IDriverDeliverService;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 司机送货预约Controller
 *
 * @author taojd
 * @date 2023-02-16
 */
@RestController
@RequestMapping("/driverDeliver")
@Api(tags = "司机送货预约接口")
public class DriverDeliverController extends BaseController {

    @Autowired
    private IDriverDeliverService driverDeliverService;

    /**
     * 查询司机送货信息列表
     *
     * @param driverDeliverDTO 查询条件
     * @return 司机送货信息列表
     */
    @GetMapping("/list")
    @ApiOperation("查询司机送货信息列表")
    public R<PageVO<DriverDeliverVO>> list(DriverDeliverDTO driverDeliverDTO) {
        List<DriverDeliverVO> list = driverDeliverService.selectDriverDeliverVO(driverDeliverDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }

}
