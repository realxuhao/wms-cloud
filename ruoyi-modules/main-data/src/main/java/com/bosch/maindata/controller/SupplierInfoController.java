package com.bosch.maindata.controller;

import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import com.bosch.maindata.service.ISupplierInfoService;
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
import com.bosch.maindata.domain.SupplierInfo;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.page.TableDataInfo;

/**
 * 供应商Controller
 * 
 * @author xuhao
 * @date 2022-09-21
 */
@RestController
@RequestMapping("/supplier")
public class SupplierInfoController extends BaseController
{
    @Autowired
    private ISupplierInfoService supplierInfoService;

    /**
     * 查询供应商列表
     */
    @RequiresPermissions("maindata:info:list")
    @GetMapping("/list")
    public TableDataInfo list(SupplierInfo supplierInfo)
    {
        startPage();
        List<SupplierInfo> list = supplierInfoService.selectSupplierInfoList(supplierInfo);
        return getDataTable(list);
    }

    /**
     * 导出供应商列表
     */
    @RequiresPermissions("maindata:info:export")
    @Log(title = "供应商", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SupplierInfo supplierInfo)
    {
        List<SupplierInfo> list = supplierInfoService.selectSupplierInfoList(supplierInfo);
        ExcelUtil<SupplierInfo> util = new ExcelUtil<SupplierInfo>(SupplierInfo.class);
        util.exportExcel(response, list, "供应商数据");
    }

    /**
     * 获取供应商详细信息
     */
    @RequiresPermissions("maindata:info:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(supplierInfoService.selectSupplierInfoById(id));
    }

    /**
     * 新增供应商
     */
    @RequiresPermissions("maindata:info:add")
    @Log(title = "供应商", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SupplierInfo supplierInfo)
    {
        return toAjax(supplierInfoService.insertSupplierInfo(supplierInfo));
    }

    /**
     * 修改供应商
     */
    @RequiresPermissions("maindata:info:edit")
    @Log(title = "供应商", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SupplierInfo supplierInfo)
    {
        return toAjax(supplierInfoService.updateSupplierInfo(supplierInfo));
    }

    /**
     * 删除供应商
     */
    @RequiresPermissions("maindata:info:remove")
    @Log(title = "供应商", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(supplierInfoService.deleteSupplierInfoByIds(ids));
    }
}
