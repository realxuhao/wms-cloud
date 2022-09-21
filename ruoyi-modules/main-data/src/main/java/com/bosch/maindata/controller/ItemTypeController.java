package com.bosch.maindata.controller;

import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import com.bosch.maindata.service.IItemTypeService;
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
import com.bosch.maindata.domain.ItemType;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.page.TableDataInfo;

/**
 * 物料类型Controller
 * 
 * @author xuhao
 * @date 2022-09-21
 */
@RestController
@RequestMapping("/item-type")
@Api(tags = "物料类型 API")
public class ItemTypeController extends BaseController
{
    @Autowired
    private IItemTypeService itemTypeService;

    /**
     * 查询物料类型列表
     */
//    @RequiresPermissions("maindata:type:list")
//    @GetMapping("/list")
//    public TableDataInfo list(ItemType itemType)
//    {
//        startPage();
//        List<ItemType> list = itemTypeService.selectItemTypeList(itemType);
//        return getDataTable(list);
//    }

    /**
     * 查询物料类型列表
     */
    @ApiOperation("获取物料类型")
    @RequiresPermissions("maindata:type:list")
    @GetMapping("/list")
    public R<List<ItemType>> list(ItemType itemType)
    {
        startPage();
        List<ItemType> list = itemTypeService.selectItemTypeList(itemType);
        return R.ok(list);
    }
    /**
     * 导出物料类型列表
     */
    @RequiresPermissions("maindata:type:export")
    @Log(title = "物料类型", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ItemType itemType)
    {
        List<ItemType> list = itemTypeService.selectItemTypeList(itemType);
        ExcelUtil<ItemType> util = new ExcelUtil<ItemType>(ItemType.class);
        util.exportExcel(response, list, "物料类型数据");
    }

    /**
     * 获取物料类型详细信息
     */
    @RequiresPermissions("maindata:type:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(itemTypeService.selectItemTypeById(id));
    }

    /**
     * 新增物料类型
     */
    @RequiresPermissions("maindata:type:add")
    @Log(title = "物料类型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ItemType itemType)
    {
        return toAjax(itemTypeService.insertItemType(itemType));
    }

    /**
     * 修改物料类型
     */
    @RequiresPermissions("maindata:type:edit")
    @Log(title = "物料类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ItemType itemType)
    {
        return toAjax(itemTypeService.updateItemType(itemType));
    }

    /**
     * 删除物料类型
     */
    @RequiresPermissions("maindata:type:remove")
    @Log(title = "物料类型", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(itemTypeService.deleteItemTypeByIds(ids));
    }
}
