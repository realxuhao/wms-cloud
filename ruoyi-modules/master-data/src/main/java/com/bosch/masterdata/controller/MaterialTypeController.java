package com.bosch.masterdata.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.bosch.masterdata.domain.dto.MaterialTypeDTO;
import com.bosch.masterdata.domain.vo.DepartmentVO;
import com.bosch.masterdata.domain.vo.MaterialTypeVO;
import com.bosch.masterdata.domain.vo.PageVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
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
import com.bosch.masterdata.domain.MaterialType;
import com.bosch.masterdata.service.IMaterialTypeService;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.page.TableDataInfo;

/**
 * 物料类型Controller
 * 
 * @author xuhao
 * @date 2022-09-22
 */
@RestController
@RequestMapping("/materialType")
public class MaterialTypeController extends BaseController
{
    @Autowired
    private IMaterialTypeService materialTypeService;

    /**
     * 查询物料类型列表
     */
    @RequiresPermissions("masterdata:type:list")
    @GetMapping("/list")
    public TableDataInfo list(MaterialType materialType)
    {
        startPage();
        List<MaterialType> list = materialTypeService.selectMaterialTypeList(materialType);
        return getDataTable(list);
    }

    /**
     * 导出物料类型列表
     */
    @RequiresPermissions("masterdata:type:export")
    @Log(title = "物料类型", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MaterialType materialType)
    {
        List<MaterialType> list = materialTypeService.selectMaterialTypeList(materialType);
        ExcelUtil<MaterialType> util = new ExcelUtil<MaterialType>(MaterialType.class);
        util.exportExcel(response, list, "物料类型数据");
    }

    /**
     * 获取物料类型详细信息
     */
    @RequiresPermissions("masterdata:type:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(materialTypeService.selectMaterialTypeById(id));
    }

    /**
     * 新增物料类型
     */
    @RequiresPermissions("masterdata:type:add")
    @Log(title = "物料类型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MaterialType materialType)
    {
        return toAjax(materialTypeService.insertMaterialType(materialType));
    }

    /**
     * 修改物料类型
     */
    @RequiresPermissions("masterdata:type:edit")
    @Log(title = "物料类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MaterialType materialType)
    {
        return toAjax(materialTypeService.updateMaterialType(materialType));
    }

    /**
     * 删除物料类型
     */
    @RequiresPermissions("masterdata:type:remove")
    @Log(title = "物料类型", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(materialTypeService.deleteMaterialTypeByIds(ids));
    }


    /**
     * 查询物料类型列表
     */
    @ApiOperation("查询启用物料类型列表")
    @GetMapping("/materialTypeVOList")
    public R<PageVO<MaterialTypeVO>> list(MaterialTypeDTO materialTypeDTO)
    {
        startPage();
        List<MaterialTypeVO> list = materialTypeService.selectMaterialTypeVOList(materialTypeDTO);
        return R.ok(new PageVO<>(list,new PageInfo<>(list).getTotal()));
    }
}
