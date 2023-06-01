package com.bosch.masterdata.controller;

import com.bosch.masterdata.api.domain.BlackDriver;
import com.bosch.masterdata.api.domain.dto.BlackDriverDTO;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.bosch.masterdata.api.domain.vo.BlackDriverVO;
import com.bosch.masterdata.service.IBlackDriverService;
import com.bosch.masterdata.utils.BeanConverUtil;
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
 * 司机黑名单Controller
 *
 * @author taojd
 * @date 2023-02-13
 */
@RestController
@RequestMapping("/driver")
@Api(tags = "司机黑名单接口")
public class BlackDriverController extends BaseController {

    @Autowired
    private IBlackDriverService blackDriverService;


    /**
     * 查询司机黑名单列表
     */
    ////@RequiresPermissions("masterdata:driver:list")
    @GetMapping("/list")
    @ApiOperation("查询司机黑名单列表")
    public R<PageVO<BlackDriverVO>> list(BlackDriverDTO blackDriverDTO) {
        startPage();
        List<BlackDriver> list = blackDriverService.selectBlackDriverList(BeanConverUtil.conver(blackDriverDTO, BlackDriver.class));
        List<BlackDriverVO> blackDriverVOS = BeanConverUtil.converList(list, BlackDriverVO.class);
        return R.ok(new PageVO<>(blackDriverVOS, new PageInfo<>(blackDriverVOS).getTotal()));
    }

    /**
     * 获取司机黑名单详细信息
     */
    ////@RequiresPermissions("masterdata:driver:get")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取司机黑名单详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(blackDriverService.selectBlackDriverById(id));
    }

    /**
     * 新增司机黑名单
     */
    ////@RequiresPermissions("masterdata:driver:add")
    @Log(title = "司机黑名单", businessType = BusinessType.INSERT)
    @ApiOperation("新增司机黑名单")
    @PostMapping
    public AjaxResult add(@RequestBody BlackDriverDTO blackDriverDTO) {
        return toAjax(blackDriverService.insertBlackDriver(BeanConverUtil.conver(blackDriverDTO, BlackDriver.class)));
    }

    /**
     * 修改司机黑名单
     */
    //@RequiresPermissions("driver:list:edit")
    @Log(title = "司机黑名单", businessType = BusinessType.UPDATE)
    @ApiOperation("修改司机黑名单")
    @PutMapping(value = "/{id}")
    public AjaxResult edit(@PathVariable("id") Long id, @RequestBody BlackDriverDTO blackDriverDTO) {
        blackDriverDTO.setDriverId(id);
        return toAjax(blackDriverService.updateBlackDriver(BeanConverUtil.conver(blackDriverDTO, BlackDriver.class)));
    }

    /**
     * 删除司机黑名单
     */
    //@RequiresPermissions("driver:list:delete")
    @Log(title = "司机黑名单", businessType = BusinessType.DELETE)
    @ApiOperation("删除司机黑名单")
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        return toAjax(blackDriverService.deleteBlackDriverById(id));
    }

    /**
     * 获取司机黑名单详细信息(根据司机wechatId查询)
     */
    @GetMapping(value = "/black/{wechatId}")
    @ApiOperation("根据司机wechatId获取司机黑名单详细信息")
    public R<List<BlackDriverVO>> getInfoByWechatId(@PathVariable("wechatId") String wechatId, boolean isBlack) {
        List<BlackDriver> blackDrivers = blackDriverService.selectBlackDriverByWechatId(wechatId, isBlack);
        List<BlackDriverVO> blackDriverVOS = BeanConverUtil.converList(blackDrivers, BlackDriverVO.class);
        return R.ok(blackDriverVOS);
    }

    @ApiOperation("保存司机名单")
    @PostMapping("save")
    public R<Boolean> save(@RequestBody BlackDriverDTO blackDriverDTO) {
        return R.ok(blackDriverService.saveBlackDriver(BeanConverUtil.conver(blackDriverDTO, BlackDriver.class)));
    }


}
