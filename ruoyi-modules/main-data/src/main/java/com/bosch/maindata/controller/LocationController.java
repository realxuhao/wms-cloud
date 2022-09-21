package com.bosch.maindata.controller;

import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import com.bosch.maindata.service.ILocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import com.bosch.maindata.domain.Location;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.page.TableDataInfo;

/**
 * 库位Controller
 * 
 * @author xuhao
 * @date 2022-09-21
 */
@RestController
@RequestMapping("/location")
public class LocationController extends BaseController
{
    @Autowired
    private ILocationService locationService;

    /**
     * 查询库位列表
     */
    @RequiresPermissions("maindata:location:list")
    @GetMapping("/list")
    public TableDataInfo list(Location location)
    {
        startPage();
        List<Location> list = locationService.selectLocationList(location);
        return getDataTable(list);
    }

    /**
     * 导出库位列表
     */
    @RequiresPermissions("maindata:location:export")
    @Log(title = "库位", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Location location)
    {
        List<Location> list = locationService.selectLocationList(location);
        ExcelUtil<Location> util = new ExcelUtil<Location>(Location.class);
        util.exportExcel(response, list, "库位数据");
    }

    /**
     * 获取库位详细信息
     */
    @RequiresPermissions("maindata:location:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(locationService.selectLocationById(id));
    }

    /**
     * 新增库位
     */
    @RequiresPermissions("maindata:location:add")
    @Log(title = "库位", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Location location)
    {
        return toAjax(locationService.insertLocation(location));
    }

    /**
     * 修改库位
     */
    @RequiresPermissions("maindata:location:edit")
    @Log(title = "库位", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Location location)
    {
        return toAjax(locationService.updateLocation(location));
    }

    /**
     * 删除库位
     */
    @RequiresPermissions("maindata:location:remove")
    @Log(title = "库位", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(locationService.deleteLocationByIds(ids));
    }
}
