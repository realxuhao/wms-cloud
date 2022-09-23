package com.bosch.masterdata.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.bosch.masterdata.domain.dto.TimeWindowDTO;
import com.bosch.masterdata.domain.vo.PageVO;
import com.bosch.masterdata.domain.vo.TimeWindowVO;
import com.bosch.masterdata.utils.BeanConverUtil;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "时间窗口接口列表")
public class TimeWindowController extends BaseController {
    @Autowired
    private ITimeWindowService timeWindowService;

    /**
     * 查询道口时间窗口列表
     */
    @RequiresPermissions("masterdata:window:list")
    @ApiOperation("查询时间窗口列表")
    @GetMapping("/list")
    public R<PageVO<TimeWindowVO>> list(TimeWindowDTO timeWindowDTO) {
        startPage();
        List<TimeWindowVO> list = timeWindowService.selectTimeWindowList(timeWindowDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }

    /**
     * 导出道口时间窗口列表
     */
    @RequiresPermissions("masterdata:window:export")
    @Log(title = "道口时间窗口", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TimeWindowDTO timeWindowDTO) {
        List<TimeWindowVO> list = timeWindowService.selectTimeWindowList(timeWindowDTO);
        ExcelUtil<TimeWindow> util = new ExcelUtil<TimeWindow>(TimeWindow.class);
        util.exportExcel(response, BeanConverUtil.converList(list, TimeWindow.class), "道口时间窗口数据");
    }

    /**
     * 获取道口时间窗口详细信息
     */
    @RequiresPermissions("masterdata:window:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(timeWindowService.selectTimeWindowById(id));
    }

    /**
     * 新增道口时间窗口
     */
    @RequiresPermissions("masterdata:window:add")
    @Log(title = "道口时间窗口", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增时间窗口")
    public AjaxResult add(@RequestBody TimeWindowDTO timeWindowDTO) {
        return toAjax(timeWindowService.insertTimeWindow(timeWindowDTO));
    }

    /**
     * 修改道口时间窗口
     */
    @RequiresPermissions("masterdata:window:edit")
    @Log(title = "道口时间窗口", businessType = BusinessType.UPDATE)
    @ApiOperation("修改时间窗口")
    @PutMapping
    public AjaxResult edit(@RequestBody TimeWindowDTO timeWindowDTO) {
        return toAjax(timeWindowService.updateTimeWindow(timeWindowDTO));
    }

    /**
     * 删除道口时间窗口
     */
    @RequiresPermissions("masterdata:window:remove")
    @Log(title = "道口时间窗口", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(timeWindowService.deleteTimeWindowByIds(ids));
    }
}
