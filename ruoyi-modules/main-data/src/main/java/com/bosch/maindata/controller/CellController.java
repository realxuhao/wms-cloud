package com.bosch.maindata.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.bosch.maindata.domain.Cell;
import com.bosch.maindata.service.ICellService;
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
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.page.TableDataInfo;

/**
 * 部门Controller
 * 
 * @author xuhao
 * @date 2022-09-21
 */
@RestController
@RequestMapping("/cell")
public class CellController extends BaseController
{
    @Autowired
    private ICellService cellService;

    /**
     * 查询部门列表
     */
    @RequiresPermissions("maindata:cell:list")
    @GetMapping("/list")
    public TableDataInfo list(Cell cell)
    {
        startPage();
        List<Cell> list = cellService.selectCellList(cell);
        return getDataTable(list);
    }

    /**
     * 导出部门列表
     */
    @RequiresPermissions("maindata:cell:export")
    @Log(title = "部门", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Cell cell)
    {
        List<Cell> list = cellService.selectCellList(cell);
        ExcelUtil<Cell> util = new ExcelUtil<Cell>(Cell.class);
        util.exportExcel(response, list, "部门数据");
    }

    /**
     * 获取部门详细信息
     */
    @RequiresPermissions("maindata:cell:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(cellService.selectCellById(id));
    }

    /**
     * 新增部门
     */
    @RequiresPermissions("maindata:cell:add")
    @Log(title = "部门", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Cell cell)
    {
        return toAjax(cellService.insertCell(cell));
    }

    /**
     * 修改部门
     */
    @RequiresPermissions("maindata:cell:edit")
    @Log(title = "部门", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Cell cell)
    {
        return toAjax(cellService.updateCell(cell));
    }

    /**
     * 删除部门
     */
    @RequiresPermissions("maindata:cell:remove")
    @Log(title = "部门", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(cellService.deleteCellByIds(ids));
    }
}
