package com.bosch.masterdata.controller;

import java.util.List;
import java.io.IOException;
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
import com.bosch.masterdata.domain.Ware;
import com.bosch.masterdata.service.IWareService;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.page.TableDataInfo;

/**
 * 仓库Controller
 * 
 * @author xuhao
 * @date 2022-09-26
 */
@RestController
@RequestMapping("/ware")
public class WareController extends BaseController
{
    @Autowired
    private IWareService wareService;

    /**
     * 查询仓库列表
     */
    @RequiresPermissions("masterdata:ware:list")
    @GetMapping("/list")
    public TableDataInfo list(Ware ware)
    {
        startPage();
        List<Ware> list = wareService.selectWareList(ware);
        return getDataTable(list);
    }

    /**
     * 导出仓库列表
     */
    @RequiresPermissions("masterdata:ware:export")
    @Log(title = "仓库", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Ware ware)
    {
        List<Ware> list = wareService.selectWareList(ware);
        ExcelUtil<Ware> util = new ExcelUtil<Ware>(Ware.class);
        util.exportExcel(response, list, "仓库数据");
    }

    /**
     * 获取仓库详细信息
     */
    @RequiresPermissions("masterdata:ware:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(wareService.selectWareById(id));
    }

    /**
     * 新增仓库
     */
    @RequiresPermissions("masterdata:ware:add")
    @Log(title = "仓库", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Ware ware)
    {
        return toAjax(wareService.insertWare(ware));
    }

    /**
     * 修改仓库
     */
    @RequiresPermissions("masterdata:ware:edit")
    @Log(title = "仓库", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Ware ware)
    {
        return toAjax(wareService.updateWare(ware));
    }

    /**
     * 删除仓库
     */
    @RequiresPermissions("masterdata:ware:remove")
    @Log(title = "仓库", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(wareService.deleteWareByIds(ids));
    }
}
