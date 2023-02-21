package com.bosch.vehiclereservation.controller;

import com.bosch.vehiclereservation.api.domain.dto.DriverDeliverDTO;
import com.bosch.vehiclereservation.api.domain.vo.DriverDeliverVO;
import com.bosch.vehiclereservation.api.domain.vo.DriverDispatchVO;
import com.bosch.vehiclereservation.api.domain.vo.PageVO;
import com.bosch.vehiclereservation.service.IDriverDispatchService;
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
 * 车辆调度Controller
 *
 * @author taojd
 * @date 2023-02-21
 */
@RestController
@RequestMapping("/driverDispatch")
@Api(tags = "车辆调度接口")
public class DriverDispatchController extends BaseController {

    @Autowired
    private IDriverDispatchService driverDispatchService;

    /**
     * 获取今天签到车辆数据
     *
     * @param wareId 仓库id
     * @return 车辆调度信息列表
     */
    @RequiresPermissions("vehiclereservation:driverdispatch:signlist")
    @GetMapping("/list")
    @ApiOperation("查询司机送货信息列表")
    public R<List<DriverDispatchVO>> getTodaySignData(Long wareId) {
        List<DriverDispatchVO> list = driverDispatchService.selectTodaySignData(wareId);
        return R.ok(list);
    }

}
