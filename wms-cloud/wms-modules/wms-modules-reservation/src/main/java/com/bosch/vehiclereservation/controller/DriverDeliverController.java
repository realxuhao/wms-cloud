package com.bosch.vehiclereservation.controller;


import com.bosch.vehiclereservation.api.domain.dto.DriverDeliverDTO;
import com.bosch.vehiclereservation.api.domain.vo.DriverDeliverVO;
import com.bosch.vehiclereservation.api.domain.vo.PageVO;
import com.bosch.vehiclereservation.service.IDriverDeliverService;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @RequiresPermissions("vehiclereservation:driverdeliver:list")
    @GetMapping("/list")
    @ApiOperation("查询司机送货信息列表")
    public R<PageVO<DriverDeliverVO>> list(DriverDeliverDTO driverDeliverDTO) {
        List<DriverDeliverVO> list = driverDeliverService.selectDriverDeliverVO(driverDeliverDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }


    /**
     * 删除司机的预约信息（只能删状态是0的数据）
     */
    @RequiresPermissions("vehiclereservation:driverdeliver:remove")
    @Log(title = "删除司机的预约信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        return toAjax(driverDeliverService.deleteDriverDeliverById(id));
    }


}
