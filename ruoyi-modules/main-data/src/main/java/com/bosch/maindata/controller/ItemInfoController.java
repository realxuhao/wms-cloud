package com.bosch.maindata.controller;

import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import com.bosch.maindata.service.IItemInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
import com.bosch.maindata.domain.ItemInfo;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.page.TableDataInfo;

/**
 * 物料信息Controller
 * 
 * @author xuhao
 * @date 2022-09-21
 */
@RestController
@RequestMapping("/item")
@Api(tags = "物料信息 API")
public class ItemInfoController extends BaseController
{
    @Autowired
    private IItemInfoService itemInfoService;

    /**
     * 查询物料信息列表
     */
    @RequiresPermissions("maindata:info:list")
    @GetMapping("/list")
    @ApiOperation("查询物料信息列表")
    @ApiResponses({@ApiResponse(code = 200,message = "OK",response = ItemInfo.class)})
    public TableDataInfo list(ItemInfo itemInfo)
    {
        startPage();
        List<ItemInfo> list = itemInfoService.selectItemInfoList(itemInfo);
        return getDataTable(list);
    }

    /**
     * 导出物料信息列表
     */
    @RequiresPermissions("maindata:info:export")
    @Log(title = "物料信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ItemInfo itemInfo)
    {
        List<ItemInfo> list = itemInfoService.selectItemInfoList(itemInfo);
        ExcelUtil<ItemInfo> util = new ExcelUtil<ItemInfo>(ItemInfo.class);
        util.exportExcel(response, list, "物料信息数据");
    }

    /**
     * 获取物料信息详细信息
     */
    @RequiresPermissions("maindata:info:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(itemInfoService.selectItemInfoById(id));
    }

    /**
     * 新增物料信息
     */
    @RequiresPermissions("maindata:info:add")
    @Log(title = "物料信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ItemInfo itemInfo)
    {
        return toAjax(itemInfoService.insertItemInfo(itemInfo));
    }

    /**
     * 修改物料信息
     */
    @RequiresPermissions("maindata:info:edit")
    @Log(title = "物料信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ItemInfo itemInfo)
    {
        return toAjax(itemInfoService.updateItemInfo(itemInfo));
    }

    /**
     * 删除物料信息
     */
    @RequiresPermissions("maindata:info:remove")
    @Log(title = "物料信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(itemInfoService.deleteItemInfoByIds(ids));
    }
}
