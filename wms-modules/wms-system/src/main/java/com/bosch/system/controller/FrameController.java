package com.bosch.system.controller;

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
import com.bosch.system.domain.Frame;
import com.bosch.system.service.IFrameService;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.page.TableDataInfo;

/**
 * 跨Controller
 * 
 * @author ruoyi
 * @date 2022-09-19
 */
@RestController
@RequestMapping("/frame")
public class FrameController extends BaseController
{
    @Autowired
    private IFrameService frameService;

    /**
     * 查询跨列表
     */
    @RequiresPermissions("system:frame:list")
    @GetMapping("/list")
    public TableDataInfo list(Frame frame)
    {
        startPage();
        List<Frame> list = frameService.selectFrameList(frame);
        return getDataTable(list);
    }

    /**
     * 导出跨列表
     */
    @RequiresPermissions("system:frame:export")
    @Log(title = "跨", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Frame frame)
    {
        List<Frame> list = frameService.selectFrameList(frame);
        ExcelUtil<Frame> util = new ExcelUtil<Frame>(Frame.class);
        util.exportExcel(response, list, "跨数据");
    }

    /**
     * 获取跨详细信息
     */
    @RequiresPermissions("system:frame:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(frameService.selectFrameById(id));
    }

    /**
     * 新增跨
     */
    @RequiresPermissions("system:frame:add")
    @Log(title = "跨", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Frame frame)
    {
        return toAjax(frameService.insertFrame(frame));
    }

    /**
     * 修改跨
     */
    @RequiresPermissions("system:frame:edit")
    @Log(title = "跨", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Frame frame)
    {
        return toAjax(frameService.updateFrame(frame));
    }

    /**
     * 删除跨
     */
    @RequiresPermissions("system:frame:remove")
    @Log(title = "跨", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(frameService.deleteFrameByIds(ids));
    }
}
