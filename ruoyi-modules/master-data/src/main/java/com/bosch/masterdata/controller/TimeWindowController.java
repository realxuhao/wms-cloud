package com.bosch.masterdata.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
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
import com.bosch.masterdata.domain.TimeWindow;
import com.bosch.masterdata.service.ITimeWindowService;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.page.TableDataInfo;

/**
 * 道口时间窗口Controller
 * 
 * @author xuhao
 * @date 2022-09-22
 */
@RestController
@RequestMapping("/timeWindow")
public class TimeWindowController extends BaseController
{
    @Autowired
    private ITimeWindowService timeWindowService;

    /**
     * 查询道口时间窗口列表
     */
    @RequiresPermissions("masterdata:window:list")
    @GetMapping("/list")
    public TableDataInfo list(TimeWindow timeWindow)
    {
        startPage();
        List<TimeWindow> list = timeWindowService.selectTimeWindowList(timeWindow);
        return getDataTable(list);
    }

    /**
     * 导出道口时间窗口列表
     */
    @RequiresPermissions("masterdata:window:export")
    @Log(title = "道口时间窗口", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TimeWindow timeWindow)
    {
        List<TimeWindow> list = timeWindowService.selectTimeWindowList(timeWindow);
        ExcelUtil<TimeWindow> util = new ExcelUtil<TimeWindow>(TimeWindow.class);
        util.exportExcel(response, list, "道口时间窗口数据");
    }

    /**
     * 获取道口时间窗口详细信息
     */
    @RequiresPermissions("masterdata:window:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(timeWindowService.selectTimeWindowById(id));
    }

    /**
     * 新增道口时间窗口
     */
    @RequiresPermissions("masterdata:window:add")
    @Log(title = "道口时间窗口", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TimeWindow timeWindow)
    {
        return toAjax(timeWindowService.insertTimeWindow(timeWindow));
    }

    /**
     * 修改道口时间窗口
     */
    @RequiresPermissions("masterdata:window:edit")
    @Log(title = "道口时间窗口", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TimeWindow timeWindow)
    {
        return toAjax(timeWindowService.updateTimeWindow(timeWindow));
    }

    /**
     * 删除道口时间窗口
     */
    @RequiresPermissions("masterdata:window:remove")
    @Log(title = "道口时间窗口", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(timeWindowService.deleteTimeWindowByIds(ids));
    }
}
