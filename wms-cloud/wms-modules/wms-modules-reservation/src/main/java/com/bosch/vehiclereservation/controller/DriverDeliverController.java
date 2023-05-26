package com.bosch.vehiclereservation.controller;


import com.bosch.vehiclereservation.api.domain.dto.DriverDeliverDTO;
import com.bosch.vehiclereservation.api.domain.dto.SupplierDTO;
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
//    @RequiresPermissions("vehiclereservation:driverdeliver:list")
    @GetMapping("/list")
    @ApiOperation("查询司机送货信息列表")
    public R<PageVO<DriverDeliverVO>> list(DriverDeliverDTO driverDeliverDTO) {
        List<DriverDeliverVO> list = driverDeliverService.selectDriverDeliverVO(driverDeliverDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }


    /**
     * 删除司机的预约信息（只能删状态是0的数据）
     */
    //@RequiresPermissions("vehiclereservation:driverdeliver:remove")
    @Log(title = "删除司机的预约信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        return toAjax(driverDeliverService.deleteDriverDeliverById(id));
    }

    /**
     * 新增司机的预约信息
     */
//    @RequiresPermissions("vehiclereservation:driverdeliver:add")
    @Log(title = "新增司机的预约信息", businessType = BusinessType.INSERT)
    @ApiOperation("新增司机的预约信息")
    @PostMapping("/add")
    public AjaxResult add(@RequestBody DriverDeliverDTO driverDeliverDTO) {
        return toAjax(driverDeliverService.insertDriverDeliver(driverDeliverDTO));
    }

    /**
     * 获取司机的预约信息
     *
     * @param wechatId 微信id
     * @return
     */
    @ApiOperation("获取司机的预约信息")
    @GetMapping("/info/{wechatId}")
    public R<List<DriverDeliverVO>> getDriverDeliverInfo(@PathVariable String wechatId) {
        return R.ok(driverDeliverService.selectDriverDeliverInfo(wechatId));
    }

    /**
     * 司机送货签到(已预约)
     *
     * @param id 主键id
     * @return
     */
    @Log(title = "司机送货签到(已预约)", businessType = BusinessType.UPDATE)
    @ApiOperation("司机送货签到(已预约)")
    @GetMapping("/signin/{id}")
    public AjaxResult signIn(@PathVariable Long id) {
        return toAjax(driverDeliverService.signIn(id));
    }


    /**
     * 司机送货签到(未预约)
     *
     * @param driverDeliverDTO 送货签到信息
     * @return
     */
    @Log(title = "司机送货签到(未预约)", businessType = BusinessType.INSERT)
    @ApiOperation("司机送货签到(未预约)")
    @PostMapping("/signin")
    public AjaxResult signInScene(@RequestBody DriverDeliverDTO driverDeliverDTO) {
        return toAjax(driverDeliverService.signInDriverDeliver(driverDeliverDTO));
    }

}
