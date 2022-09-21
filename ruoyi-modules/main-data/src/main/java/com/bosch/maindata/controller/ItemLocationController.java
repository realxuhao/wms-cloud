package com.bosch.maindata.controller;

import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import com.bosch.maindata.service.IItemLocationService;
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
import com.bosch.maindata.domain.ItemLocation;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.page.TableDataInfo;

/**
 * 物料库位分配策略Controller
 * 
 * @author xuhao
 * @date 2022-09-21
 */
@RestController
@RequestMapping("/item-location")
public class ItemLocationController extends BaseController
{
    @Autowired
    private IItemLocationService itemLocationService;

    /**
     * 查询物料库位分配策略列表
     */
    @RequiresPermissions("maindata:location:list")
    @GetMapping("/list")
    public TableDataInfo list(ItemLocation itemLocation)
    {
        startPage();
        List<ItemLocation> list = itemLocationService.selectItemLocationList(itemLocation);
        return getDataTable(list);
    }

    /**
     * 导出物料库位分配策略列表
     */
    @RequiresPermissions("maindata:location:export")
    @Log(title = "物料库位分配策略", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ItemLocation itemLocation)
    {
        List<ItemLocation> list = itemLocationService.selectItemLocationList(itemLocation);
        ExcelUtil<ItemLocation> util = new ExcelUtil<ItemLocation>(ItemLocation.class);
        util.exportExcel(response, list, "物料库位分配策略数据");
    }

    /**
     * 获取物料库位分配策略详细信息
     */
    @RequiresPermissions("maindata:location:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(itemLocationService.selectItemLocationById(id));
    }

    /**
     * 新增物料库位分配策略
     */
    @RequiresPermissions("maindata:location:add")
    @Log(title = "物料库位分配策略", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ItemLocation itemLocation)
    {
        return toAjax(itemLocationService.insertItemLocation(itemLocation));
    }

    /**
     * 修改物料库位分配策略
     */
    @RequiresPermissions("maindata:location:edit")
    @Log(title = "物料库位分配策略", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ItemLocation itemLocation)
    {
        return toAjax(itemLocationService.updateItemLocation(itemLocation));
    }

    /**
     * 删除物料库位分配策略
     */
    @RequiresPermissions("maindata:location:remove")
    @Log(title = "物料库位分配策略", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(itemLocationService.deleteItemLocationByIds(ids));
    }
}
