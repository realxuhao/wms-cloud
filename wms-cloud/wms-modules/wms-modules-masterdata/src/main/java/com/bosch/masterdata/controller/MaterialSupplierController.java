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
import com.bosch.masterdata.api.domain.MaterialSupplier;
import com.bosch.masterdata.service.IMaterialSupplierService;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.page.TableDataInfo;

/**
 * 供应商物料Controller
 * 
 * @author xuhao
 * @date 2022-09-22
 */
@RestController
@RequestMapping("/materialSupplier")
public class MaterialSupplierController extends BaseController
{
    @Autowired
    private IMaterialSupplierService materialSupplierService;

    /**
     * 查询供应商物料列表
     */
    //@RequiresPermissions("masterdata:supplier:list")
    @GetMapping("/list")
    public TableDataInfo list(MaterialSupplier materialSupplier)
    {
        startPage();
        List<MaterialSupplier> list = materialSupplierService.selectMaterialSupplierList(materialSupplier);
        return getDataTable(list);
    }

    /**
     * 导出供应商物料列表
     */
    //@RequiresPermissions("masterdata:supplier:export")
    @Log(title = "供应商物料", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MaterialSupplier materialSupplier)
    {
        List<MaterialSupplier> list = materialSupplierService.selectMaterialSupplierList(materialSupplier);
        ExcelUtil<MaterialSupplier> util = new ExcelUtil<MaterialSupplier>(MaterialSupplier.class);
        util.exportExcel(response, list, "供应商物料数据");
    }

    /**
     * 获取供应商物料详细信息
     */
    //@RequiresPermissions("masterdata:supplier:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(materialSupplierService.selectMaterialSupplierById(id));
    }

    /**
     * 新增供应商物料
     */
    //@RequiresPermissions("masterdata:supplier:add")
    @Log(title = "供应商物料", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MaterialSupplier materialSupplier)
    {
        return toAjax(materialSupplierService.insertMaterialSupplier(materialSupplier));
    }

    /**
     * 修改供应商物料
     */
    //@RequiresPermissions("masterdata:supplier:edit")
    @Log(title = "供应商物料", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MaterialSupplier materialSupplier)
    {
        return toAjax(materialSupplierService.updateMaterialSupplier(materialSupplier));
    }

    /**
     * 删除供应商物料
     */
    //@RequiresPermissions("masterdata:supplier:remove")
    @Log(title = "供应商物料", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(materialSupplierService.deleteMaterialSupplierByIds(ids));
    }
}
