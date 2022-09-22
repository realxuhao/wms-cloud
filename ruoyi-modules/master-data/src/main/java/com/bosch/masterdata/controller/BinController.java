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
import com.bosch.masterdata.domain.Bin;
import com.bosch.masterdata.service.IBinService;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.page.TableDataInfo;

/**
 * 库位Controller
 * 
 * @author xuhao
 * @date 2022-09-22
 */
@RestController
@RequestMapping("/bin")
public class BinController extends BaseController
{
    @Autowired
    private IBinService binService;

    /**
     * 查询库位列表
     */
    @RequiresPermissions("masterdata:bin:list")
    @GetMapping("/list")
    public TableDataInfo list(Bin bin)
    {
        startPage();
        List<Bin> list = binService.selectBinList(bin);
        return getDataTable(list);
    }

    /**
     * 导出库位列表
     */
    @RequiresPermissions("masterdata:bin:export")
    @Log(title = "库位", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Bin bin)
    {
        List<Bin> list = binService.selectBinList(bin);
        ExcelUtil<Bin> util = new ExcelUtil<Bin>(Bin.class);
        util.exportExcel(response, list, "库位数据");
    }

    /**
     * 获取库位详细信息
     */
    @RequiresPermissions("masterdata:bin:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(binService.selectBinById(id));
    }

    /**
     * 新增库位
     */
    @RequiresPermissions("masterdata:bin:add")
    @Log(title = "库位", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Bin bin)
    {
        return toAjax(binService.insertBin(bin));
    }

    /**
     * 修改库位
     */
    @RequiresPermissions("masterdata:bin:edit")
    @Log(title = "库位", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Bin bin)
    {
        return toAjax(binService.updateBin(bin));
    }

    /**
     * 删除库位
     */
    @RequiresPermissions("masterdata:bin:remove")
    @Log(title = "库位", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(binService.deleteBinByIds(ids));
    }
}
