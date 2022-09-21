package com.bosch.maindata.controller;

import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import com.bosch.maindata.service.IMoveTypeConfigService;
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
import com.bosch.maindata.domain.MoveTypeConfig;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.page.TableDataInfo;

/**
 * 移动类型配置Controller
 * 
 * @author xuhao
 * @date 2022-09-21
 */
@RestController
@RequestMapping("/move-type")
public class MoveTypeConfigController extends BaseController
{
    @Autowired
    private IMoveTypeConfigService moveTypeConfigService;

    /**
     * 查询移动类型配置列表
     */
    @RequiresPermissions("maindata:config:list")
    @GetMapping("/list")
    public TableDataInfo list(MoveTypeConfig moveTypeConfig)
    {
        startPage();
        List<MoveTypeConfig> list = moveTypeConfigService.selectMoveTypeConfigList(moveTypeConfig);
        return getDataTable(list);
    }

    /**
     * 导出移动类型配置列表
     */
    @RequiresPermissions("maindata:config:export")
    @Log(title = "移动类型配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MoveTypeConfig moveTypeConfig)
    {
        List<MoveTypeConfig> list = moveTypeConfigService.selectMoveTypeConfigList(moveTypeConfig);
        ExcelUtil<MoveTypeConfig> util = new ExcelUtil<MoveTypeConfig>(MoveTypeConfig.class);
        util.exportExcel(response, list, "移动类型配置数据");
    }

    /**
     * 获取移动类型配置详细信息
     */
    @RequiresPermissions("maindata:config:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(moveTypeConfigService.selectMoveTypeConfigById(id));
    }

    /**
     * 新增移动类型配置
     */
    @RequiresPermissions("maindata:config:add")
    @Log(title = "移动类型配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MoveTypeConfig moveTypeConfig)
    {
        return toAjax(moveTypeConfigService.insertMoveTypeConfig(moveTypeConfig));
    }

    /**
     * 修改移动类型配置
     */
    @RequiresPermissions("maindata:config:edit")
    @Log(title = "移动类型配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MoveTypeConfig moveTypeConfig)
    {
        return toAjax(moveTypeConfigService.updateMoveTypeConfig(moveTypeConfig));
    }

    /**
     * 删除移动类型配置
     */
    @RequiresPermissions("maindata:config:remove")
    @Log(title = "移动类型配置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(moveTypeConfigService.deleteMoveTypeConfigByIds(ids));
    }
}
