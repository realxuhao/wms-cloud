package com.bosch.masterdata.controller;

import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import com.bosch.masterdata.domain.dto.AreaDTO;
import com.bosch.masterdata.domain.dto.MaterialDTO;
import com.bosch.masterdata.domain.vo.AreaVO;
import com.bosch.masterdata.domain.vo.MaterialVO;
import com.bosch.masterdata.domain.vo.PageVO;
import com.github.pagehelper.PageInfo;
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
import com.bosch.masterdata.domain.Area;
import com.bosch.masterdata.service.IAreaService;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.page.TableDataInfo;

/**
 * 区域Controller
 * 
 * @author xuhao
 * @date 2022-09-26
 */
@Api(tags = "存储区接口")
@RestController
@RequestMapping("/area")
public class AreaController extends BaseController
{
    @Autowired
    private IAreaService areaService;

    /**
     * 查询区域列表
     */
//    @RequiresPermissions("masterdata:area:list")
//    @GetMapping("/list")
//    public TableDataInfo list(Area area)
//    {
//        startPage();
//        List<Area> list = areaService.selectAreaList(area);
//        return getDataTable(list);
//    }

    /**
     * 导出区域列表
     */
    @RequiresPermissions("masterdata:area:export")
    @Log(title = "区域", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Area area)
    {
        List<Area> list = areaService.selectAreaList(area);
        ExcelUtil<Area> util = new ExcelUtil<Area>(Area.class);
        util.exportExcel(response, list, "区域数据");
    }

    /**
     * 获取区域详细信息
     */
    //@RequiresPermissions("masterdata:area:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return AjaxResult.success(areaService.selectAreaById(id));
    }

    /**
     * 新增区域
     */
//    @RequiresPermissions("masterdata:area:add")
//    @Log(title = "区域", businessType = BusinessType.INSERT)
//    @PostMapping
//    public AjaxResult add(@RequestBody Area area)
//    {
//        return toAjax(areaService.insertArea(area));
//    }

    /**
     * 修改区域
     */
//    @RequiresPermissions("masterdata:area:edit")
//    @Log(title = "区域", businessType = BusinessType.UPDATE)
//    @PutMapping
//    public AjaxResult edit(@RequestBody Area area)
//    {
//        return toAjax(areaService.updateArea(area));
//    }

    /**
     * 删除区域
     */
    @RequiresPermissions("masterdata:area:remove")
    @Log(title = "区域", businessType = BusinessType.DELETE)
    @ApiOperation("删除区域")
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        return toAjax(areaService.deleteAreaByIds(ids));
    }

    /**
     * 查询区域信息
     */
    @ApiOperation("查询区域信息")
    @GetMapping("/areaVOList")
    public R<PageVO<AreaVO>> list(AreaDTO areaDTO)
    {
        startPage();
        List<AreaVO> list = areaService.selectAreaVOList(areaDTO);

        return R.ok(new PageVO<>(list,new PageInfo<>(list).getTotal()));
    }

    /**
     * 新增区域
     */
    //@RequiresPermissions("masterdata:area:add")
    @ApiOperation("新增区域")
    @Log(title = "区域", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AreaDTO areaDTO)
    {
        return toAjax(areaService.insertArea(areaDTO));
    }

    /**
     * 修改区域
     */
    //@RequiresPermissions("masterdata:area:edit")
    @ApiOperation("修改区域")
    @Log(title = "区域", businessType = BusinessType.UPDATE)
    @PutMapping("/{id}")
    public AjaxResult edit(@PathVariable("id") Integer id,@RequestBody AreaDTO areaDTO)
    {
        areaDTO.setId(id);
        return toAjax(areaService.updateArea(areaDTO));
    }
}
