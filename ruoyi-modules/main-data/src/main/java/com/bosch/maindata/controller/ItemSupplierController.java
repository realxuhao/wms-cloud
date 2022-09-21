package com.bosch.maindata.controller;

import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import com.bosch.maindata.service.IItemSupplierService;
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
import com.bosch.maindata.domain.ItemSupplier;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.page.TableDataInfo;

/**
 * 供应商物料Controller
 * 
 * @author xuhao
 * @date 2022-09-21
 */
@RestController
@RequestMapping("/item-supplier")
public class ItemSupplierController extends BaseController
{
    @Autowired
    private IItemSupplierService itemSupplierService;

    /**
     * 查询供应商物料列表
     */
    @RequiresPermissions("maindata:supplier:list")
    @GetMapping("/list")
    public TableDataInfo list(ItemSupplier itemSupplier)
    {
        startPage();
        List<ItemSupplier> list = itemSupplierService.selectItemSupplierList(itemSupplier);
        return getDataTable(list);
    }

    /**
     * 导出供应商物料列表
     */
    @RequiresPermissions("maindata:supplier:export")
    @Log(title = "供应商物料", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ItemSupplier itemSupplier)
    {
        List<ItemSupplier> list = itemSupplierService.selectItemSupplierList(itemSupplier);
        ExcelUtil<ItemSupplier> util = new ExcelUtil<ItemSupplier>(ItemSupplier.class);
        util.exportExcel(response, list, "供应商物料数据");
    }

    /**
     * 获取供应商物料详细信息
     */
    @RequiresPermissions("maindata:supplier:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(itemSupplierService.selectItemSupplierById(id));
    }

    /**
     * 新增供应商物料
     */
    @RequiresPermissions("maindata:supplier:add")
    @Log(title = "供应商物料", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ItemSupplier itemSupplier)
    {
        return toAjax(itemSupplierService.insertItemSupplier(itemSupplier));
    }

    /**
     * 修改供应商物料
     */
    @RequiresPermissions("maindata:supplier:edit")
    @Log(title = "供应商物料", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ItemSupplier itemSupplier)
    {
        return toAjax(itemSupplierService.updateItemSupplier(itemSupplier));
    }

    /**
     * 删除供应商物料
     */
    @RequiresPermissions("maindata:supplier:remove")
    @Log(title = "供应商物料", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(itemSupplierService.deleteItemSupplierByIds(ids));
    }
}
