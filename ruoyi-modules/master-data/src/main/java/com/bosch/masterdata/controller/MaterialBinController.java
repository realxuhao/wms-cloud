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
import com.bosch.masterdata.domain.MaterialBin;
import com.bosch.masterdata.service.IMaterialBinService;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.page.TableDataInfo;

/**
 * 物料库位分配策略Controller
 * 
 * @author xuhao
 * @date 2022-09-22
 */
@RestController
@RequestMapping("/materialBin")
public class MaterialBinController extends BaseController
{
    @Autowired
    private IMaterialBinService materialBinService;

    /**
     * 查询物料库位分配策略列表
     */
    @RequiresPermissions("masterdata:bin:list")
    @GetMapping("/list")
    public TableDataInfo list(MaterialBin materialBin)
    {
        startPage();
        List<MaterialBin> list = materialBinService.selectMaterialBinList(materialBin);
        return getDataTable(list);
    }

    /**
     * 导出物料库位分配策略列表
     */
    @RequiresPermissions("masterdata:bin:export")
    @Log(title = "物料库位分配策略", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MaterialBin materialBin)
    {
        List<MaterialBin> list = materialBinService.selectMaterialBinList(materialBin);
        ExcelUtil<MaterialBin> util = new ExcelUtil<MaterialBin>(MaterialBin.class);
        util.exportExcel(response, list, "物料库位分配策略数据");
    }

    /**
     * 获取物料库位分配策略详细信息
     */
    @RequiresPermissions("masterdata:bin:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(materialBinService.selectMaterialBinById(id));
    }

    /**
     * 新增物料库位分配策略
     */
    @RequiresPermissions("masterdata:bin:add")
    @Log(title = "物料库位分配策略", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MaterialBin materialBin)
    {
        return toAjax(materialBinService.insertMaterialBin(materialBin));
    }

    /**
     * 修改物料库位分配策略
     */
    @RequiresPermissions("masterdata:bin:edit")
    @Log(title = "物料库位分配策略", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MaterialBin materialBin)
    {
        return toAjax(materialBinService.updateMaterialBin(materialBin));
    }

    /**
     * 删除物料库位分配策略
     */
    @RequiresPermissions("masterdata:bin:remove")
    @Log(title = "物料库位分配策略", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(materialBinService.deleteMaterialBinByIds(ids));
    }
}
